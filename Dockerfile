# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jre-jammy

WORKDIR /opt/ostock

COPY target/*.jar ./licensing-service.jar

ENTRYPOINT java -jar ./licensing-service.jar
