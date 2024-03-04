# GitHub repositories

## About The Project
This service operates as a RESTful API, and its purpose is to furnish data concerning user repositories using the GitHub public API.
It enables users to obtain a list of repositories associated with a specified username, inclusive of details about their respective branches.

### Built With

- Spring Boot
- WebFlux
- Java 21
- Junit 5 with Wiremock
- Docker
- API first design - API generated based on swagger specification

## Requirements

- Java 21
- Maven

## Setup

To get started with this project after cloning the repository, follow these steps.
1. Ensure you have Java Development Kit (JDK) 21 installed on your machine.

## Running the Application

You can run this project in several ways:

### Using an IDE

1. Open the project in your preferred IDE that supports Maven, such as IntelliJ IDEA, Eclipse, or VSCode.
2. Locate the `GithubRepositoryApplication.java` file that contains the `main` method.
3. Run the application using the IDE's built-in tools.

### Using Docker

1. Package the application with Maven:
    1. Ensure you are in the project root directory and run:
    2. ``mvn clean package``
2. Build the Docker image:
    1. After packaging, build the Docker image with:
    2. ``docker build -t git-hub-repositories .``
3. Run the application in a Docker container:
    1. Start the container using:
    2. ``docker run -p 8080:8080 git-hub-repositories``
4. The application will be accessible at http://localhost:8080 and the Swagger UI can be accessed at http://localhost:8080/webjars/swagger-ui/index.html.


### Using Maven Wrapper

For Unix/Linux systems:

```bash
./mvnw spring-boot:run
```

For Windows systems:
```cmd
mvnw.cmd spring-boot:run
```

## Testing

This project uses JUnit and Mockito for unit testing and integration testing. To run the tests, follow these steps:

1. Navigate to the project root directory in your terminal or command prompt.
2. Execute the following command to run all tests:

```bash
mvnw test
```

This command will run all unit and integration tests in the project and provide a summary of the test results.

## API Documentation

The OpenAPI documentation for this project is available in the `openapi.yaml` file located at:

```
openapi/openapi.yaml
```
Additionally, you can view and interact with the API's endpoints through the Swagger UI, which is accessible at:

[http://localhost:8080/webjars/swagger-ui/index.html(http://localhost:8080/webjars/swagger-ui/index.html)

This web interface provides a detailed look at all available RESTful endpoints, allowing you to test them directly from your browser. It's an invaluable tool for both development and testing phases.

### Higher requests rate to GitHub API
By default, this application will use unauthenticated requests to the GitHub API. Unauthenticated requests are subject to a lower rate limit, as explained in the [GitHub API Rate Limit documentation](https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting).

If this application needs to handle a higher request rate to the GitHub API, it is recommended to configure authentication with access token.
More information can be found here -> https://docs.github.com/en/rest/guides/getting-started-with-the-rest-api?apiVersion=2022-11-28.

Access token should be set as property in `application.yaml` config -> 'github-api.token'.