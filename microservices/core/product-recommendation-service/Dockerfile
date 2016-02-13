FROM java:8
LABEL  srai.micro.service="product-recommendation-service" srai.micro.project="true"
VOLUME /tmp
RUN mkdir /build
ADD build/libs/product-recommendation-service-0.0.1-SNAPSHOT.jar /build/product-recommendation-service-0.0.1-SNAPSHOT.jar
RUN bash -c 'touch /product-recommendation-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/build/product-recommendation-service-0.0.1-SNAPSHOT.jar"]