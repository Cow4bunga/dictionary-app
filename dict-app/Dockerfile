FROM openjdk:17-alpine

ADD target/text-service-0.0.1-SNAPSHOT.jar text-service.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","text-service.jar"]