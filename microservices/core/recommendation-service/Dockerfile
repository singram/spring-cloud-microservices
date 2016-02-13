FROM java:8
LABEL  srai.micro.service="recommendation-service"
VOLUME /tmp
RUN mkdir /build
ADD build/libs/recommendation-service-0.0.1-SNAPSHOT.jar /build/recommendation-service-0.0.1-SNAPSHOT.jar
RUN bash -c 'touch /recommendation-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/build/recommendation-service-0.0.1-SNAPSHOT.jar"]