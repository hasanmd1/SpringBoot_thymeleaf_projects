# Spring Boot Thymeleaf Docker Projects

## Overview

Spring Boot-based web applications that use Thymeleaf as the template engine for rendering dynamic HTML pages. And are packaged and deployed using Docker, with the project managed via **Maven** or **Gradle** build tools. Continuous Integration and Continuous Deployment (CI/CD) are configured with **GitHub Actions** and **SonarQube** for code quality analysis.

## Features

- **Spring Boot** for building Java-based backend services.
- **Thymeleaf** as a templating engine for dynamic content rendering.
- **Docker** for containerizing the application for easy deployment.
- **GitHub** for version control and CI/CD workflows.
- **SonarQube** integration for continuous code quality analysis.
- **Maven** and **Gradle** as build tools for flexibility in managing dependencies and building the application.

## Project Setup

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Java 21+** (JDK)
- **Docker** (for containerization)
- **Maven** (for dependency management and building with Maven)
- **Gradle** (for building with Gradle)
- **Git/GitHub** (for version control)
- **SonarQube** (for code quality analysis, if running locally)

### Cloning the Project

To get started, clone this repository using Git:

```bash
git clone https://github.com/hasanmd1/SpringBoot_thymeleaf_projects.git
cd SpringBoot_thymeleaf_projects
```

## Running the Application

### Running with Maven

To build and run the application using Maven, follow these steps:

1. **Build the application:**

   ```bash
   mvn clean install
   ```

2. **Run the application:**

   ```bash
   mvn spring-boot:run
   or
   java -jar <app_name>
   ```

3. Open your browser and go to `http://localhost:8080` to see the application running.

### Running with Gradle

To build and run the application using Gradle, follow these steps:

1. **Build the application:**

   ```bash
   ./gradlew build
   ```

2. **Run the application:**

   ```bash
   ./gradlew bootRun
   or java -jar <application_name>
   ```

3. Open your browser and go to `http://localhost:8080` to see the application running.

### Docker

To run the application in a Docker container:

1. **Build the Docker image:**

   ```bash
   docker build -t <image> .
   ```

2. **Run the Docker container:**

   ```bash
   docker run -p 8080:8080 <image>
   ```
3. **Simply:**

```bash
docker-compose up
```

4. Open your browser and go to `http://localhost:8080` to see the application running.


## CI/CD with GitHub Actions

The project is configured to use **GitHub Actions** for Continuous Integration (CI). It automatically builds, tests, and performs code analysis with **SonarQube** on each commit or pull request.

### Building Docker Image

To build the Docker image:

```bash
docker build -t <image_name> .
```

To run the Docker container:

```bash
docker run -p 8080:8080 <image_name>
```

or, to simplify,
```bash
docker-compose up
```

## Technologies Used

- **Spring Boot**: Java framework for building enterprise-level applications.
- **Thymeleaf**: Templating engine for rendering dynamic HTML content.
- **Docker**: Containerization of the application.
- **GitHub**: Version control and CI/CD setup.
- **SonarQube**: Code quality analysis.
- **Maven** & **Gradle**: Build tools for dependency management and application building.
- **JUnit**: Unit testing framework.
- **GitHub Actions**: CI/CD automation.
