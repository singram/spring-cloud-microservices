#!/bin/bash

CMD=$1
shift
PARAMS=$*
CURRENT_BRANCH_NAME=`git rev-parse --abbrev-ref HEAD`
SCRIPT_NAME=ms
export DOCKER_HOST_IP=`ifconfig docker0 | grep "inet addr" | awk -F'[: ]+' '{ print $4 }'`

function help {
    echo "Usage:"
    echo "    ./${SCRIPT_NAME} build"
    echo "    ./${SCRIPT_NAME} clean"
    echo "    ./${SCRIPT_NAME} docker_host_ip"
    echo "    ./${SCRIPT_NAME} redis_clear"
    echo "    ./${SCRIPT_NAME} redis_load_data"
    echo "    ./${SCRIPT_NAME} redis_monitor"
    echo "    ./${SCRIPT_NAME} portals"
    echo "    ./${SCRIPT_NAME} purge"
    echo "    ./${SCRIPT_NAME} purge_all"
    echo "    ./${SCRIPT_NAME} run_elk"
    echo "    ./${SCRIPT_NAME} run"
    echo "    ./${SCRIPT_NAME} stats"
    echo
}

function build {
    cd common;                                            ./gradlew clean build publishToMavenLocal; cd -
    cd microservices/core/person-service;                 ./gradlew clean build; cd -
    cd microservices/core/person-recommendation-service;  ./gradlew clean build; cd -
    cd microservices/core/product-recommendation-service; ./gradlew clean build; cd -
    cd microservices/aggregate/person-composite-service;  ./gradlew clean build; cd -
    cd microservices/support/spring-boot-admin;           ./gradlew clean build; cd -
    cd microservices/support/hystrix-dashboard;           ./gradlew clean build; cd -
    cd microservices/support/turbine;                     ./gradlew clean build; cd -
    cd microservices/support/eureka;                      ./gradlew clean build; cd -
    cd microservices/support/zuul;                        ./gradlew clean build; cd -

    find . -name *SNAPSHOT.jar -exec du -h {} \;

    docker-compose -f rm
    docker-compose build
}

case $CMD in
    "build")
        build
        ;;
    "clean")
        echo "Removing unneeded containers and images..."
        docker rm `docker ps --no-trunc -aq`
        docker images -q --filter "dangling=true" | xargs docker rmi
        ;;
    "docker_host_ip")
        echo $DOCKER_HOST_IP
        ;;
    "help")
        help
        ;;
    "redis_clear")
        echo "Clear redis"
        docker exec -it springcloudmicroservices_person-service-redis_1 redis-cli FLUSHDB
        ;;
    "redis_load_data")
        echo "Load test data into redis"
        ./microservices/core/person-service/load_test_data $PARAMS
        ;;
    "redis_monitor")
        echo "Monitoring redis"
        docker exec -it springcloudmicroservices_person-service-redis_1 redis-cli MONITOR
        ;;
    "portals")
        echo "Viewing management portals..."
        firefox http://localhost:8001 \
                http://localhost:8004 \
                http://localhost:8000/hystrix \
                http://localhost:8000/hystrix/monitor?stream=http%3A%2F%2Fperson-composite-service%3A8080%2Fhystrix.stream \
                http://localhost:8000/hystrix/monitor?stream=http%3A%2F%2Fturbine-service%3A8989%2Fturbine.stream \
                http://localhost:80/ \
                http://localhost:4040/ \
                https://github.com/singram/spring-cloud-microservices &
        ;;
    "purge")
        echo "Removing api containers and images..."
        docker rm $(docker ps --no-trunc -aq --filter="name=springcloudmicroservices")
        docker rmi -f $(docker images -q --filter="label=srai.micro.project=true")
        ;;
    "purge_all")
        echo "Removing all docker containers and images..."
        docker rm -f $(docker ps -a -q)
        docker rmi -f $(docker images -q)
        ;;
    "run")
        echo "Starting services..."
        exec docker-compose up
        ;;
    "run_elk")
        echo "Starting services..."
        exec docker-compose -f docker-compose-elk.yml up
        ;;
    "stats")
        echo "Displaying docker stats..."
        docker stats $(docker ps  | sed 1d | awk '{print $NF}')
        ;;
    *)
        echo "I don't know what you want me to do"
        help
        ;;
esac
