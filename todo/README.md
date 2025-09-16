# Todo Application

A full-stack todo application built with Spring Boot and vanilla JavaScript.

## Features

- ✨ Modern responsive web interface
- 👤 User registration and authentication
- 📝 Create, read, update, delete todos
- 🔍 Search and filter todos
- 📊 User statistics dashboard
- 🎨 Beautiful gradient UI design

## Technology Stack

### Backend
- **Spring Boot 3.5.5** - Main framework
- **Spring Data JPA** - Database abstraction
- **H2 Database** - In-memory database
- **Spring Boot Actuator** - Health checks and monitoring
- **OpenAPI/Swagger** - API documentation

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling with gradients and animations
- **Vanilla JavaScript** - Interactivity and API integration

## Quick Start

### Using Docker (Recommended)

1. **Build and run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

2. **Or build and run manually:**
   ```bash
   # Build the Docker image
   docker build -t todo-app .
   
   # Run the container
   docker run -p 8080:8080 todo-app
   ```

3. **Access the application:**
   - Frontend: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Health Check: http://localhost:8080/actuator/health

### Local Development

1. **Prerequisites:**
   - Java 21 or higher
   - Gradle 8.x

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

3. **Access the application:**
   - Frontend: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:todo_db`
     - Username: `sa`
     - Password: (empty)

## API Endpoints

### User Management
- `POST /v1/user/create` - Create new user
- `GET /v1/user` - Get all users
- `GET /v1/user/{id}` - Get user by ID
- `GET /v1/user/username/{username}` - Get user by username
- `PUT /v1/user/{id}` - Update user
- `DELETE /v1/user/{id}` - Delete user

### Todo Management
- `POST /v1/todo/create` - Create new todo
- `GET /v1/todo` - Get all todos
- `GET /v1/todo/{id}` - Get todo by ID
- `GET /v1/todo/user/{userId}` - Get todos by user
- `PUT /v1/todo/{id}` - Update todo
- `DELETE /v1/todo/{id}` - Delete todo
- `GET /v1/todo/search` - Search todos
- `GET /v1/todo/statistics/user/{userId}` - Get user statistics

## Project Structure

```
src/
├── main/
│   ├── java/com/example/todo/
│   │   ├── TodoApplication.java
│   │   ├── config/
│   │   │   ├── CorsConfig.java
│   │   │   └── OpenApiConfig.java
│   │   └── module/
│   │       ├── user/
│   │       │   ├── controller/UserController.java
│   │       │   ├── service/UserService.java
│   │       │   ├── repository/UserRepository.java
│   │       │   ├── entity/User.java
│   │       │   └── dto/UserDto.java
│   │       └── todo/
│   │           ├── controller/TodoController.java
│   │           ├── service/TodoService.java
│   │           ├── repository/TodoRepository.java
│   │           ├── entity/Todo.java
│   │           └── dto/TodoDto.java
│   └── resources/
│       ├── static/
│       │   ├── index.html
│       │   ├── styles.css
│       │   └── script.js
│       └── application.properties
└── test/
```

## Docker Configuration

The application includes optimized Docker configuration:

- **Multi-stage build** for smaller image size
- **Non-root user** for security
- **Health checks** for container monitoring
- **JVM optimization** for container environments
- **.dockerignore** for efficient builds

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_PORT` | `8080` | Application port |
| `SPRING_PROFILES_ACTIVE` | `default` | Spring profile |
| `JAVA_OPTS` | `-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport` | JVM options |

## Development

### Build
```bash
./gradlew build
```

### Test
```bash
./gradlew test
```

### Clean
```bash
./gradlew clean
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.