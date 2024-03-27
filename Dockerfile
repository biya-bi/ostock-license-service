# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base AS test
RUN ./mvnw test

FROM base AS debug
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base AS package
RUN ./mvnw package

FROM eclipse-temurin:17-jdk-jammy AS build
COPY --from=package /app/target/licensing-service-*.jar /licensing-service.jar
RUN mkdir -p target && (cd target; jar -xf /licensing-service.jar)

FROM eclipse-temurin:17-jre-jammy AS run
ARG OUTPUT_DIR=target
COPY --from=build ${OUTPUT_DIR}/BOOT-INF/lib /opt/app/lib/
COPY --from=build ${OUTPUT_DIR}/META-INF /opt/app/META-INF/
COPY --from=build ${OUTPUT_DIR}/BOOT-INF/classes /opt/app/
CMD ["java", "-cp", "/opt/app:/opt/app/lib/*", "com.optimagrowth.license.LicensingServiceApplication"]