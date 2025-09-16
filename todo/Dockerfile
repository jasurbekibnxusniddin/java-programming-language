# Multi-stage build for Java application
FROM gradle:8.10-jdk21 AS build

# Set working directory
WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src/

# Make gradlew executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build -x test --no-daemon

# Production stage
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Create non-root user for security
RUN addgroup -S todoapp && adduser -S todoapp -G todoapp

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown -R todoapp:todoapp /app

# Switch to non-root user
USER todoapp

# Expose port
EXPOSE 8080

# Optional JVM arguments for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]