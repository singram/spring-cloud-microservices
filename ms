#!/bin/bash

CMD=$1
shift
PARAMS=$*
CURRENT_BRANCH_NAME=`git rev-parse --abbrev-ref HEAD`
SCRIPT_NAME=ms

function help {
    echo "Usage:"
    echo "    ./${SCRIPT_NAME} build"
    echo "    ./${SCRIPT_NAME} clean"
    echo "    ./${SCRIPT_NAME} run"
    echo
}

function build {
    cd common;                                            ./gradlew clean build publishToMavenLocal; cd -
    cd microservices/core/person-service;                 ./gradlew clean build; cd -
    cd microservices/core/person-recommendation-service;  ./gradlew clean build; cd -
    cd microservices/core/product-recommendation-service; ./gradlew clean build; cd -
    cd microservices/aggregate/person-composite-service;  ./gradlew clean build; cd -
    cd microservices/support/hystrix-dashboard;           ./gradlew clean build; cd -

    find . -name *SNAPSHOT.jar -exec du -h {} \;
}

case $CMD in
    "run")
        echo "Starting services..."
        exec docker-compose up
        ;;
    "clean")
        echo "Removing unneeded containers and images..."
        docker rm `docker ps --no-trunc -aq`
        docker images -q --filter "dangling=true" | xargs docker rmi
        ;;
    "help")
        help
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
    "build")
        build
        ;;
    *)
        echo "I don't know what you want me to do"
        help
        ;;
esac
