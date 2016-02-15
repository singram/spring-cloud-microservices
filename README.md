# spring-cloud-microservices
Experiments in spring cloud.

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

    sudo apt-get install jq apache2-utils

### To run

Execute the following to run the services.

    ./ms build
    ./ms run

Note, there are a fair number of services, mostly java, and as such they have a reasonably hefty memory requirement on aggregate.

### To test Hystrix circuit breaking

1. Build and run the system `./ms build && ./ms run`
2. View the Hystrix dashboard `firefox http://localhost:8090` and view the 'person-composite-service' hystrix stream @ `http://person-composite-service:8080/hystrix.stream`
3. Generate some load `ab -n 10000 -c 10 -l http://localhost:8083/2`
4. Adjust performance of underlying services
    curl  "localhost:808[0-1]/set-processing-time?minMs=1000&maxMs=2000" | jq .
   or disable a service
    docker-compose [un]pause [person-service|person-recommendation-service|product-recommendation-service]
5. Observer Hystrix dashboard for impact

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

### Monitoring
- https://www.brianchristner.io/how-to-setup-prometheus-docker-monitoring/

### Load testing
- http://dak1n1.com/blog/14-http-load-generate/
- http://gatling.io/#/
- https://blazemeter.com/blog/open-source-load-testing-tools-which-one-should-you-use

### Undertow vs Tomcat
http://www.alexecollins.com/spring-boot-performance/

## Credits

Insprired heavily by
http://callistaenterprise.se/blogg/teknik/2015/04/10/building-microservices-with-spring-cloud-and-netflix-oss-part-1/