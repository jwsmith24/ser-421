# Survey API in Spring Boot

## Requirements 
This app uses a Postgres Docker container for persistence that is managed with Flyway migrations. 
Migration V5 (found in `/main/resources/db/migration`) seeds the database with data IAW project 
requirements. If you're running in a Mac or Windows environment, ensure Docker Desktop is running.

## Running the application

Start the Spring Boot app by following the steps below:
1) Navigate to the root directory of the project
2) Spin up the Postgres Docker container in the background by running `docker compose up -d`
3) Start the spring app by running `./gradlew bR`

## API Documentation

Once the application is running, interactive API documentation can be found at 
http://localhost:8080/swagger-ui/index.html

