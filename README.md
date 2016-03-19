# spring-cloud-microservices
Experiments in spring cloud and the Netflix OSS stack

## Areas to experiment in
- Hystrix (DONE)
- Zuul
- Feign (DONE)
- Eureka (DONE)/Consul
- Vault
- Dynamic configuration
- ELK (DONE)

## Setup

Pre-requisites
- Java 1.8
- Docker
- Docker-compose

### Installation (debian base)

#### Install Docker

    apt-get install apparmor lxc cgroup-lite
    wget -qO- https://get.docker.com/ | sh
    sudo usermod -aG docker YourUserNameHere
    sudo service docker restart

#### Install Docker-compose  (1.6+)

*MAKE SURE YOU HAVE AN UP TO DATE VERSION OF DOCKER COMPOSE*

To check the version:

    docker-compose --version

To install the 1.6.0:

    sudo su
    curl -L https://github.com/docker/compose/releases/download/1.6.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    exit

Install supporting tools

    sudo apt-get install jq apache2-utils jmeter

### To run

Start the ELK service which receives all docker logs

    ./ms run_elk

Execute the following to run the services.

    ./ms build
    ./ms run

Note, there are a fair number of services, mostly java, and as such they have a reasonably hefty memory requirement on aggregate.

Run `./ms redis_load_data` to populate test data.  The needs to be run only once.

### Gradle daemon

To accelerate local development, it is recommended to run gradle daemonized.  This is as simple as running the following

    echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties

### To test Hystrix circuit breaking

1. Build and run the system `./ms build && ./ms run`
2. Check memory and uptime via `./ms stats`.  When the system is stable and all services have started CPU usage on all processes should be nominal.
3. Generate some load `ab -n 100000 -c 13 -l http://localhost:8080/composite/[async/]2`.  There is also a simple JMeter load script under `\configuration`
4. Adjust performance of underlying services

    curl  "localhost:8080/person/set-processing-time?minMs=1000&maxMs=2000" | jq .
    curl  "localhost:8080/person_rec/set-processing-time?minMs=1000&maxMs=2000" | jq .
    curl  "localhost:8080/product_rec/set-processing-time?minMs=1000&maxMs=2000" | jq .

   or adjust service error rate

    curl  "localhost:8080[person|person_rec|product_rec]/set-error?percentage=20" | jq .

   or disable a service

    docker-compose [un]pause [person-service|person-recommendation-service|product-recommendation-service]

5. Observe Hystrix dashboard for impact `./ms portals` (shows eureka, hystrix & turbine dashboards)

### Grafana setup

Hystrix and JVM metrics can be gathered for instrumentation trending and displayed via the following steps

1. Navigate to `localhost:80` in a browser.  Login is `admin` \ `admin`
2. Create a new data source for graphite.  The url is `http:\\localhost:81`.  Verify connectivity.
3. Import the two dashboards found under the `\configuration\grafana` directory.  Rememeber to update the time period appropriately.

While Hystric collects metrics which can be pushed via standard means to statsd and other solutions, Turbine does not appear to support the same meterics gathering at this time meaing metrics from all services need to be pushed and centrally aggregated in statsd rather than keys off Turbine which would be prefered.

### Hystrix notes

- Thread pools are defined by group not by command.  Threadpool bulkheads should be considered in the context of sequential call protection vs concurrent
- Thread pool saturation must be handled outside of Hystrix.  By it's fundamental nature, if there is not a thread in the pool available Hystrix by definition cannot handle it, even if only to use a fallback.

### Turbine - Hystrix Stream aggregation

- While turbine v1 support property based configuration, turbine v2 only supports discovery explicitly via Eureka.  Patches seem slow to be propagated by Netflix team.
 - Spring Cloud Turbine will support Consul on next release
- Eureka client versions referenced in 1.0.6 are incompatible with the Brixton.M4 Eureka service and should be excluded.

### ELK Notes

- ELK is available to analyse the main spring boot application logs
- This is acheived via the docker GELF log driver in a trivially simple manner

#### ELK CAVEATS
- Solution requires communication through docker host
- Solution requires ELK to be available before dependant containers are started
- GELF is not secure on the wire
- GELF is less problematic using UDP and as such is likely to drop messages
- Current setup is lossy.  If ELK is down messages will be lost

### Eureka Notes

- Clients are now auto-deregistered upon shutdown
- Clients connect to Eureka in bootstrap phase of spring-cloud app as as such need to be identified correctly.
 - The bootstrap phase provides a convenient hook to centralized configuration management services such as Vault
 - Query services `curl -s -H "Accept: application/json" http://localhost:8761/eureka/apps | jq '.applications.application[]'`

## Service overview

| Service                        | Port   | Description  |
| -------------                  | ------ | ------------ |
| person-service                 | 8080   | Basic micro service with redis backend |
| person-recommendation-service  | 8081   | Micro service stub |
| product-recommendation-service | 8082   | Micro service stub |
| person-composite-service       | 8083   | Composite service calling above 3 microservices |
| hystrix-dashboard              | 8000   | SSE visualization tool (Hystrix specific) |
| eureka                         | 8001   | Service discovery service |
| turbine                        | 8002/3 | SSE aggregation service |
| redis                          | 6379   | Backing person service persistance |

## References

### Spring Boot Cloud/Netflix OSS
- https://netflix.github.io/
- http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html

### Service discovery
- http://technologyconversations.com/2015/09/08/service-discovery-zookeeper-vs-etcd-vs-consul/
- http://cloud.spring.io/spring-cloud-consul/#quick-start
- http://gliderlabs.com/registrator/latest/user/quickstart/

### Hystrix
- https://github.com/Netflix/Hystrix/wiki/Configuration
- https://ahus1.github.io/hystrix-examples/manual.html
- https://github.com/Xorlev/crankshaftd
- http://1.bp.blogspot.com/-JaBO-Dk9oNI/VMzVWnxeP_I/AAAAAAAABOA/bYBwau60AVw/s1600/hystrix-grafana.png

### Instrumentation
- https://objectpartners.com/2015/05/07/intelligent-microservice-metrics-with-spring-boot-and-statsd/
- https://github.com/ryantenney/metrics-spring

### Docker host Monitoring
- https://www.brianchristner.io/how-to-setup-docker-monitoring/
- https://www.brianchristner.io/how-to-setup-prometheus-docker-monitoring/
- https://github.com/vegasbrianc/prometheus

### Load testing
- http://dak1n1.com/blog/14-http-load-generate/
- http://gatling.io/#/
- https://blazemeter.com/blog/open-source-load-testing-tools-which-one-should-you-use

### ELK
- http://elk-docker.readthedocs.org/#running-with-docker-compose
- http://nathanleclaire.com/blog/2015/04/27/automating-docker-logging-elasticsearch-logstash-kibana-and-logspout/
- https://github.com/gliderlabs/logspout
- http://www.labouisse.com/how-to/2015/09/14/elk-and-docker-1-8/
- http://www.labouisse.com/how-to/2015/09/23/elk-docker-and-spring-boot/
- http://knes1.github.io/blog/2015/2015-08-16-manage-spring-boot-logs-with-elasticsearch-kibana-and-logstash.html

### Undertow vs Tomcat
- http://www.alexecollins.com/spring-boot-performance/

### Vault
- https://github.com/markramach/spring-boot-starter-vault

### Smaller Java 8 docker images & memory
- https://developer.atlassian.com/blog/2015/08/minimal-java-docker-containers/
- https://github.com/cloudimmunity/docker-slim
- https://spring.io/blog/2015/12/10/spring-boot-memory-performance
- https://blog.takipi.com/lean-mean-java-virtual-machine-making-your-docker-7x-lighter-with-alpine-linux/
- http://blog.yohanliyanage.com/2015/05/docker-clean-up-after-yourself/

## Credits

Insprired heavily by
- http://callistaenterprise.se/blogg/teknik/2015/04/10/building-microservices-with-spring-cloud-and-netflix-oss-part-1/
