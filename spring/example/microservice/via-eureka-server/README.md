# 1. Creating Eureka Server
## Step 1: Create the Eureka Server project

### 1. Open Spring Initializr
Here we’ll generate our Eureka Server project.
**Project Settings:**
| Setting          | Value                 |
| ---------------- | --------------------- |
| **Project**      | Gradle - Groovy       |
| **Language**     | Java                  |
| **Spring Boot**  | 3.2.x (latest stable) |
| **Group**        | com.example           |
| **Artifact**     | eureka-server         |
| **Name**         | Eureka Server         |
| **Packaging**    | Jar                   |
| **Java version** | 21 (or any)           |

### 2. Add Dependencies
**Click on “Add Dependencies” and select:**
- Spring Web 🌐 → so you can run a web service.
- Eureka Server 🧭 → to enable the discovery server itself.

Then click Generate — it’ll download a ZIP file.
Unzip it anywhere you like, and open it in your IDE (VS Code, IntelliJ, etc.).

generated project:
```scheme
├── build
│   ├── classes
│   │   └── java
│   │       ├── main
│   │       └── test
│   └── resources
│       └── main
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── HELP.md
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── eureka_server
    │   │               └── EurekaServerApplication.java
    │   └── resources
    │       ├── application.yml
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── example
                    └── eureka_server
                        └── EurekaServerApplicationTests.java
```
settings.gradle file looks like as this:
```gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.5.6'
	id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
description = 'Demo project for Spring Boot'

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

ext {
	set('springCloudVersion', "2025.0.0")
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}

tasks.named('test') {
	useJUnitPlatform()
}
```
### Step 2: Add Eureka Server annotation (turning your app into a Eureka Discovery Server)
**Open the main class:**
> src/main/java/com/example/eurekaserver/EurekaServerApplication.java

**Replace its contents with this:**
```java
package com.example.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  // 🔥 This turns your app into a Eureka Discovery Server!
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```
This single annotation @EnableEurekaServer transforms your app into a Discovery Server — the central registry where all microservices (clients) will register themselves.

### Step 3: Configure application settings
Open src/main/resources/application.yml (or application.properties)
and replace its content with:
```yml
server:
  port: 8761  # default Eureka Server port

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false   # ❌ this app itself is the server, so don’t register
    fetch-registry: false         # ❌ this app doesn’t fetch other services
  server:
    enable-self-preservation: false  # optional, for dev testing only
```

**Explanation:**
- The server runs on port 8761.
- register-with-eureka and fetch-registry are disabled because this instance is the server.
- The dashboard will be available at http://localhost:8761/

### Step 4: Run the server
In your terminal:
```bash
./gradlew bootRun
```

**After a few seconds, open your browser:**
> http://localhost:8761

You’ll see the Eureka dashboard.

**It should say:**
> “Currently, there are no instances available”
(because we haven’t registered any client yet — that’s okay!)

### Step 5: Done — Eureka Server is ready!
You now have a working Service Registry.
Next, we’ll create our first microservice client (like user-service) and make it register automatically to this Eureka Server.