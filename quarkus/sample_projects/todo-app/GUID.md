# Todo App Guide

This document provides a comprehensive guide to the Todo App, explaining its creation, architecture, dependencies, and how to run it.

## 1. Project Creation

This project is built using [Quarkus](https://quarkus.io/), a Kubernetes Native Java stack tailored for OpenJDK HotSpot and GraalVM.

It was likely initialized using the Quarkus CLI or Maven plugin, for example:

```bash
quarkus create app uz.app:todo-app \
    --extension="hibernate-orm-panache,rest,rest-jackson,jdbc-postgresql"
```

## 2. Dependencies Explained

The `pom.xml` file contains several dependencies. Here is why each one is included:

### Core & Web
- **`quarkus-rest`**: The core framework for building RESTful Web APIs (an implementation of JAX-RS). It handles HTTP requests, responses, and routing.
- **`quarkus-rest-jackson`**: Adds JSON support to REST APIs. It uses the Jackson library to serialize Java objects to JSON and deserialize JSON to Java objects automatically.
- **`quarkus-smallrye-openapi`**: Generates OpenAPI (Swagger) documentation for your API. It allows you to visualize and test your endpoints via the Swagger UI.
- **`quarkus-arc`**: Implementation of CDI (Contexts and Dependency Injection). It manages the lifecycle of beans (services, repositories, controllers) and handles dependency injection (`@Inject`).
- **`quarkus-config-yaml`**: **Crucial for YAML Config**. This dependency is required to use `application.yml` instead of the default `application.properties`. Without this, Quarkus will not recognize your YAML configuration file.

### Data & persistence
- **`quarkus-hibernate-orm-panache`**: dramatically simplifies data persistence. It allows us to use the "Active Record" pattern (entities with static methods like `Todo.listAll()`) or the "Repository" pattern, removing most boilerplate JPA code.
- **`quarkus-hibernate-orm`**: The standard Jakarta Persistence (JPA) implementation. Panache sits on top of this.
- **`quarkus-jdbc-postgresql`**: The JDBC driver required to connect to a PostgreSQL database.
- **`quarkus-hibernate-validator`**: Implements Bean Validation (Hibernate Validator). It allows us to use annotations like `@NotBlank`, `@Email`, `@NotNull` to validate data before it reaches our business logic.

### Testing
- **`quarkus-junit5`**: Enables writing tests using JUnit 5 in the Quarkus environment. It supports `@QuarkusTest` to spin up the application context for testing.
- **`rest-assured`**: A fluent Java library for testing REST APIs. It makes it easy to send HTTP requests and validate responses (status codes, body content) in tests.

## 3. How to Run

### Development Mode
To run the application in development mode with live coding enabled:
```bash
./mvnw quarkus:dev
```
- **API**: [http://localhost:8080/todo](http://localhost:8080/todo) or [http://localhost:8080/users](http://localhost:8080/users)
- **Swagger UI**: [http://localhost:8080/q/swagger-ui/](http://localhost:8080/q/swagger-ui/)
- **Dev UI**: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)

### Testing
To run the automated tests:
```bash
./mvnw test
```

### Packaging
To build the application:
```bash
./mvnw package
```
This produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.

### Native Image
If you have GraalVM installed, you can build a native executable:
```bash
./mvnw package -Dnative
```

## 4. Managing Dependencies

You can add new extensions (dependencies) easily using the Quarkus CLI or Maven wrapper.

### Using Quarkus CLI
If you have the Quarkus CLI installed:
```bash
quarkus extension add <extension-name>
# Example:
quarkus extension add hibernate-validator
```

### Using Maven Wrapper
If you don't have the CLI, use the Maven wrapper included in the project:
```bash
./mvnw quarkus:add-extension -Dextensions="<extension-name>"
# Example:
./mvnw quarkus:add-extension -Dextensions="hibernate-validator"
```

This command automatically finds the correct version and adds it to your `pom.xml`.

## 5. Security Implementation Guide

This project implements standard **JDBC-based Authentication** using `quarkus-elytron-security-jdbc`.

### Step 1: Add Dependency
We added the Elytron JDBC extension to `pom.xml`:
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-elytron-security-jdbc</artifactId>
</dependency>
```

### Step 2: Update User Entity
We modified `User.java` to include credentials.
- Added `password` field (stores Bcrypt hash).
- Added `role` field (defines authority, default "user").

### Step 3: Configure Security Realm
We configured the `jdbc` realm in `application.yml` to tell Quarkus how to find users and verify passwords.

```yaml
security:
    jdbc:
        enabled: true
        principal-query:
            # Query to fetch password and role by email
            sql: "SELECT password, role FROM users WHERE email = ?"
            bcrypt-password-mapper:
                enabled: true
                password-index: 1 # 1st column in SELECT
            attribute-mapping:
                role: 2           # 2nd column in SELECT
```

### Step 4: Secure Business Logic
- **Hashing**: Updated `UserService.create()` to hash passwords using `BcryptUtil.bcryptHash(password)` before saving.
- **Access Control**:
    - Annotating Controllers or Methods with `@Authenticated` ensures only logged-in users can access them.
    - Annotating with `@PermitAll` allows public access (e.g., for registration).

### Step 5: Testing
Unit tests in `UserResourceTest` were updated to use `given().auth().preemptive().basic(email, password)` to simulate logged-in requests.