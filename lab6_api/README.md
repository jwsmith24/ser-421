# Activity 1: GitHub API Calls

See [labRest_act1.http](labRest_act1.http) for all required calls in the IntelliJ HTTP client.

# Activity 2: Survey API in Spring Boot

## Requirements 
This app uses a Postgres image in a Docker container for persistence that is managed with Flyway 
migrations. If you're running in a Mac or Windows environment, ensure Docker Desktop is running 
before trying to spin up the container.

## Running the application

Start the Spring Boot app by following the steps below:
1) Navigate to the root directory of the project
2) Spin up the Postgres Docker container in the background by running `docker compose up -d`
3) Start the spring app by running: 
 - `./gradlew bR` on Mac/Linux
 - `gradlew.bat bR` on Windows

_Note: The database is persistent within the container across restarts and seeded IAW assignment 
requirements._

## API Documentation

Once the application is running, interactive API documentation (via Open API) can be found with 
payload schemas at: http://localhost:8080/swagger-ui/index.html

## Testing with the Postman Collection

- Navigate to the Postman collection [here](https://jwsmith24-7505503.postman.co/workspace/Jake's-Workspace~53510070-2bd9-4b3c-858f-24a969fa236e/collection/48915036-553c45eb-0c36-4c36-ba17-b922ee7f31bb?action=share&source=copy-link&creator=48915036)

If the link doesn't work, I also included the json file that can be imported directly into 
Postman. Find it under [resources.](./src/main/resources/api_lab_postman_collection.json)

I also included the Intellij Http client file I used locally for testing the end points (I'm not 
able to download postman desktop on my computer to test that they work properly). Find it under 
[resources.](./src/main/resources/Survey_API_Lab_Tests.http)

- Depending on which order you execute API calls, you may need to change Ids based on the 
  entities you create.