# Spring Boot Microservice Example

A Spring Boot RESTful Microservice that uses the following components:

- **Maven** as the build system of choice.
- **Apache Tomcat** (via **Spring Boot Web MVC**) for the servlet backend.
- **Jackson** (via **Spring Boot Web MVC**) for JSON marshalling and unmarshalling.
- **MapStruct** for automating object mapping.
- **Lombok** for generating accessors, mutators, constructors, and other Java boilerplate.
- **Spring Security** for endpoint security.
- **Spring HATEOAS** for formatted HAL JSON responses.
- **Spring Boot Actuator** for generating healthcheck endpoints.
- **Hibernate Validator 6** (via **Spring Boot Validation**) for automated validation.
- **Hibernate ORM + JPA** (via **Spring Boot Data JPA** and **JDBC**) for SQL generation and marshalling.
- **H2 Database** as a test in-memory SQL database.

## What does it do?

This project provides several CRUD endpoints for the creation, storage, updating, querying, and removal
of user accounts. This is provided as a proof of concept and example of usage within Spring Boot.

## Endpoints

An [Insomnia](http://insomnia.rest) bundle is included for convenience.

## Running the project

If you use IntelliJ, there are several run configurations already included.

You can also run directly from Maven: `mvn spring-boot:run`.

The application will default to the `local` profile which uses the in-memory H2 database,
but further development could be performed to introduce staging and production
profiles should this service ever be used in a deployed environment.
