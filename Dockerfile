# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jre-alpine

WORKDIR /opt/ostock

COPY target/*.jar ./license-service.jar

ENTRYPOINT java -jar ./license-service.jar
