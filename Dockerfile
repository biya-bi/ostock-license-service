# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base AS test
RUN ["./mvnw", "test"]

FROM base AS development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base AS build
RUN ["./mvnw", "package"]

FROM eclipse-temurin:17-jdk-jammy AS artifacts
COPY --from=build /app/target/licensing-service-*.jar /licensing-service.jar
RUN mkdir -p target && (cd target; jar -xf /licensing-service.jar)

FROM eclipse-temurin:17-jre-jammy AS production
ARG TARGET=target
COPY --from=artifacts ${TARGET}/BOOT-INF/lib /app/lib
COPY --from=artifacts ${TARGET}/META-INF /app/META-INF
COPY --from=artifacts ${TARGET}/BOOT-INF/classes /app
EXPOSE 8080
CMD ["java", "-cp", "app:app/lib/*", "com.optimagrowth.license.LicensingServiceApplication"]