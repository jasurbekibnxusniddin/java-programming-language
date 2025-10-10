# syntax=docker/dockerfile:1.4
FROM eclipse-temurin:24-jdk-alpine AS build

WORKDIR /app

RUN apk add --no-cache bash curl unzip

# Put Gradle cache in /cache so we can mount it with BuildKit
ENV GRADLE_USER_HOME=/cache/gradle

# Copy only build files first (leverage Docker cache for deps)
COPY gradlew .
COPY gradle gradle/
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

# Download dependencies into the BuildKit cache (preserves gradle dists & caches)
RUN --mount=type=cache,id=gradle-cache,target=/cache/gradle \
    ./gradlew --no-daemon dependencies || true

# Copy source last (changes often)
COPY src src/

# Build using the same cache mount so previously downloaded artifacts are reused
# RUN --mount=type=cache,id=gradle-cache,target=/cache/gradle \
#     ./gradlew clean build -x test --no-daemon

RUN ./gradlew clean build -x test

# ---------- runtime ----------
FROM eclipse-temurin:24-jre-alpine

WORKDIR /app

RUN apk add --no-cache curl \
    && addgroup -S court && adduser -S judge -G court

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown -R judge:court /app

USER judge

ARG HTTP_PORT=8081
EXPOSE ${HTTP_PORT}

# ENV JAVA_OPTS="-Xmx512m"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
