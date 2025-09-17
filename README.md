# license-service
## Environment variables
Running the **license-service** microservice requires setting the below environment variables.
- SPRING_PROFILES_ACTIVE: This environment variable should contain the Spring profiles to activate.
- SPRING_CLOUD_CONFIG_URI: This environment variable should contain the URI of the config service.
- EUREKA_URI: This environment variable should contain the URI of the Eureka service discovery endpoint for the default availability zone.
- GOOGLE_JWT_ISSUER_URI: This environment variable should contain the URI of the Google JWT issuer.
- KEYCLOAK_JWT_ISSUER_URI: This environment variable should contain the URI of the Keycloak JWT issuer.
- DATASOURCE_URL_FILE: This environment variable should contain the path to a file containing the database URL.
- DATASOURCE_USERNAME_FILE: This environment variable should contain the path to a file containing the database username.
- DATASOURCE_PASSWORD_FILE: This environment variable should contain the path to a file containing the database password.
## Sample Environment Variable File
A sample environment variable file that can be used to run the service on a local development environment could contain the below content:
```
#!/bin/bash

ENVIRONMENT=local

SECRETS_DIR="$HOME"/Development/Projects/nguiland/.vscode/ostock/secrets/"$ENVIRONMENT"
POSTGRES_SECRETS_DIR="$SECRETS_DIR/postgres"
mkdir -p "$POSTGRES_SECRETS_DIR"

export SPRING_PROFILES_ACTIVE=default
export SPRING_CLOUD_CONFIG_URI=http://localhost:9001
export EUREKA_URI=http://localhost:9002/eureka
export GOOGLE_JWT_ISSUER_URI=https://accounts.google.com
export KEYCLOAK_JWT_ISSUER_URI=http://keycloak.infra:8080/realms/ostock
export DATASOURCE_URL_FILE="$POSTGRES_SECRETS_DIR"/url
export DATASOURCE_USERNAME_FILE="$POSTGRES_SECRETS_DIR"/user
export DATASOURCE_PASSWORD_FILE="$POSTGRES_SECRETS_DIR"/password
```
## Running Postgres on a local development machine
The **license-service** requires a Postgres database. A quick way of launching such a database is by running a Docker container. Below is a sample of scripts that might be helpful for this purpose:
```
POSTGRES_PORT=9005
POSTGRES_DATA="$HOME"/Development/Projects/nguiland/.vscode/postgres/data
mkdir -p "$POSTGRES_DATA"
docker run -d --name ostock-postgres \
  -p "$POSTGRES_PORT":5432 \
  -e POSTGRES_DB=ostock \
  -e POSTGRES_USER=ostkpg \
  -e POSTGRES_PASSWORD=Passw0rd \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  -v "$POSTGRES_DATA":/var/lib/postgresql/data \
  postgres:17.6
```
## Running on a local development terminal
Assuming the sample environment variable file is named **.license-service**, the below commands can be used to run the microservice on a local development terminal
```
source .license-service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9007
```
## Running a mock JWT issuer
A mock JWT issuer can be run on a local development environment using the below command:
```
npx oauth2-mock-server -a localhost -p 9004
```
For example, a Keycloak JWT issuer can be mocked by running the above command and then setting the **KEYCLOAK_JWT_ISSUER_URI** environment variable to **http://localhost:9004**

