# Bankservices

## Introduction

Welcome to the API and microservices documentation for the bank microservice project. These services are designed with a
strong emphasis on security and performance, strictly following industry best practices. It consists
of three distinct services, each serving a specific purpose.

- **Accountservice**: Facilitates customer integration with the platform using account.
- **Loanservice**: Used for creating a new debt associated with a customer or fetching the existing data.
- **Cardservice**: Maintains data regarding existing credit card of a customer or issuing a new one.

Apart from these services, other services required for the microservices network such as an Edge server (AKA API gateway) for accessing the service network as well as Netflix Eureka server is used for service discovery and registration.

## Table of Contents

- [Project structure](#project-structure)
- [Database](#database)
  - [Definition](#definition)
    - [Schema](#schema)
  - [Technical choices](#technical-choices)
  - [ORM](#orm)
  - [Initial data dump](#initial-data-dump)
- [API Endpoints](#api-endpoints)
- [Resiliency and Security](resiliency-and-security)
- [Image building](#image-building)
- [JVM options](#jvm-options)
- [Exception handling](#exception-handling)
- [Validation and Input sanitization](validation-and-input-sanitization)
- [API documentation](api-documentation)
- [Running the project](#running-the-project)
  - [Dependencies](#dependencies)
- [Areas for Improvement](#to-dos-and-areas-for-improvement)

## Project structure

The project hierarchy adheres to standard Java package conventions.
The controllers, DTOs, exceptions and responses are organized on their own packages to enhance code separation
and security:

```
├── accountservice/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── northstar/
│   │       │       └── cosmos/
│   │       │           └── accountservice/
│   │       │               ├── audit/
│   │       │               ├── controller/
│   │       │               ├── dto/
│   │       │               ├── entity/
│   │       │               ├── exception/
│   │       │               ├── mapper/
│   │       │               ├── repository/
│   │       │               ├── service/
│   │       │               │   ├── client/
│   │       └── resources/
│   │           ├── static/
│   │           ├── templates/
├── cardservice/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── northstar/
│   │       │       └── cosmos/
│   │       │           └── cardservice/
│   │       │               ├── audit/
│   │       │               ├── controller/
│   │       │               ├── dto/
│   │       │               ├── entity/
│   │       │               ├── exception/
│   │       │               ├── mapper/
│   │       │               ├── repository/
│   │       │               ├── service/
│   │       └── resources/
│   │           ├── static/
│   │           ├── templates/
├── configserver/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── northstar/
│   │       │       └── cosmos/
│   │       │           └── configserver/
│   │       └── resources/
│   │           ├── config/
├── docker-compose/
│   ├── default/
│   ├── prod/
│   └── qa/
├── eurekaserver/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── northstar/
│   │       │       └── cosmos/
│   │       │           └── eurekaserver/
│   │       └── resources/
├── gatewayserver/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── northstar/
│   │       │       └── cosmos/
│   │       │           └── gatewayserver/
│   │       │               ├── controller/
│   │       └── resources/
├── loanservice/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── northstar/
│   │       │       └── cosmos/
│   │       │           └── loanservice/
│   │       │               ├── audit/
│   │       │               ├── controller/
│   │       │               ├── dto/
│   │       │               ├── entity/
│   │       │               ├── exception/
│   │       │               ├── mapper/
│   │       │               ├── repository/
│   │       │               ├── service/
│   │       └── resources/
│   │           ├── static/
│   │           ├── templates/
```

## Database

This project uses H2 database for the storage. Since H2 stores the data in-memory, the data will be lost when the service is stopped.
I have used MySQL docker container as database for persistence for some time during development.
But since each service has it's own standalone database, the number of docker containers running simultaneously
increases as we add new services. Since this project uses the local machine for running all the containers instead
of the cloud, H2 database is preferred as it's lightweight and has a low memory footprint.

### Definition

The database consists of 4 tables (including 1 for migration):

- **customer**: Represents customers using the platform.
- **account**: Customer account details.
- **loan**: Customer debt details.
- **card**: Customer credit card details.

### Schema

```
CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` int AUTO_INCREMENT  PRIMARY KEY,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile_number` varchar(20) NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(20) NOT NULL,
  `updated_at` date DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL
);
```

```
CREATE TABLE IF NOT EXISTS `account` (
  `customer_id` int NOT NULL,
  `account_number` int AUTO_INCREMENT  PRIMARY KEY,
  `account_type` varchar(100) NOT NULL,
  `branch_address` varchar(200) NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(20) NOT NULL,
  `updated_at` date DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL
);
```

```
CREATE TABLE IF NOT EXISTS `loan` (
  `loan_id` int NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(15) NOT NULL,
  `loan_number` varchar(100) NOT NULL,
  `loan_type` varchar(100) NOT NULL,
  `total_loan` int NOT NULL,
  `amount_paid` int NOT NULL,
  `outstanding_amount` int NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(20) NOT NULL,
  `updated_at` date DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`loan_id`)
);
```

```
CREATE TABLE IF NOT EXISTS `card` (
  `card_id` int NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(15) NOT NULL,
  `card_number` varchar(100) NOT NULL,
  `card_type` varchar(100) NOT NULL,
  `total_limit` int NOT NULL,
  `amount_used` int NOT NULL,
  `available_amount` int NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(20) NOT NULL,
  `updated_at` date DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`card_id`)
);
```

#### Technical choices

- By default, all tables include `created_by`, `updated_by`, `created_at`, and `updated_at` columns.
- No use of Dockerfile directly, instead use Buildpacks/Google Jib for enhanced security and efficiency.
- H2 database instead of persistent storage like MySQL/Postgres, as it's lightweight and has a very low memory footprint.
  (It's inmemory, hence data does not persist when service is shutdown)

### ORM

- Hibernate JPA is the ORM used since it integrates with Spring nicely.
- All transactions are set at the service layer to ensure consistency.

### Initial data dump

- For the sake of simplicity I have not added any data when the instances of the services are spinned up for the first
  time.
- But since H2 stores the data in-memory, the data will be lost when the service is stopped. Also there will not be any
  prepopulated data to play with the APIs, so I might prepolulate some data in the future.

## API endpoints

### Import Postman collection to quickly get started

- I have used HTTPie instead of Postman during development, and it currently doesn't allow exporting the API requests along with the metadata (body, headers, auth etc.,).
- I plan to generate a postman collection, but for the time being, it's not a priority.

### Getewayserver

- This acts as the entrypoint into the service network and provides APIs which forward the client request into the appropriate
  microservices.
- It uses client side load-balancing by leveraging Open Feign client.

#### Other APIs are also available but it is recommended to use only the APIs exposed by the gateway server. This is not enforced as of now, but I plan to restrict APIs which are not exposed through the edge server it in the future.

- Configserver
- Accountservice
- Loanservice
- Cardservice
- Eurekaserver

### Resiliency and Security

To ensure the resilency of the services and for enhanced security, the following measures are implemented:

- **Rate Limiting**: Implemented request rate limiting to protect against excessive API requests.
- **Circuit breaker**: Implemented circuit breakers to fail fast and avoid blocking of resources due to slow response.
- **Retry**: Implemented retries to ensure high availability during transient issues such as temporary network/resource issues.
- **No Dockerfile**: This project uses Google Jib and Buildpacks instead of custom written Dockerfile which is more prone to security
  vulnerabilities and less efficient and unoptimized.

### Image building

The application utilizes [Buildpack](https://buildpacks.io/) to construct a production-ready image
optimized for runtime efficiency, embedding security best practices and resource-aware container
capabilities.

[Google Jib](https://github.com/GoogleContainerTools/jib) is also used as it is super fast and takes less time compared to buildpacks
to generate the images. It also works without Docker daemon, so that you can directly build Docker image from within Maven or Gradle and push to any registry of your choice.

I have used Bash script to automatically clean and generate the docker images for each service when invoked so that whenever any changes
are made, generating the images is as easy as running this script.

### JVM options

- The memory configurations are done automatically thanks to buildpack.

## Exception Handling

Exception handling is centralized in the `GlobalExceptionHandler` class inside the exception package to ensure consistent error responses. The same package also houses custom exceptions for specific situations.

## Validation and Input sanitization

Input validation is performed using Bean validation and appropriate error messages are returned with the response in case of data model
violation.

## API documentation

All the APIs are documented as per the OpenAPI specification and is available through Swagger-UI

## Running the project

### Dependencies

The main dependencies of the project are:

- Java version >= 21
- Spring Boot 3
- Spring Framework 6
- [Docker](https://www.docker.com)
- [Docker Compose](https://docs.docker.com/compose/)
- [Maven](https://maven.apache.org/)
- Resilience4J
- Project Lombok
- Spring Cloud (config, client, Gateway etc.,) and more

### To Do's and Areas for Improvement

- **Caching**: To enhance performance - plan to use Caffeine for simplicity instead of Redis.
- **Logging**: Very much required for Observability and Monitoring
- **Observability and Monitoring**: Grafana and Loki might be a good choice.
- **Tests**: Where are the tests?!.
- **Authentication and Authorization**: Because the services needs to be secure and security is a necessity.
- **Native image generation**: Switch from JIT to AOT compilation by switching to GraalVM and generate native images and benchmark against the JIT version of the project.
- **Schema migration**: Use Liquibase or Flyway for database migration.
