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

### To run

Execute the following to run the services.

    ./ms build
    docker-compose up

Note, there are a fair number of services, mostly java, and as such they have a reasonably hefty memory requirement on aggregate.

## References

https://netflix.github.io/
http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html

### Service discovery
http://technologyconversations.com/2015/09/08/service-discovery-zookeeper-vs-etcd-vs-consul/
http://cloud.spring.io/spring-cloud-consul/#quick-start
http://gliderlabs.com/registrator/latest/user/quickstart/

### Hystrix

https://github.com/Netflix/Hystrix/wiki/Configuration

### Monitoring
https://www.brianchristner.io/how-to-setup-prometheus-docker-monitoring/

## Credits

Insprired heavily by
http://callistaenterprise.se/blogg/teknik/2015/04/10/building-microservices-with-spring-cloud-and-netflix-oss-part-1/