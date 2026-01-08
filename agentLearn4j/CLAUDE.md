# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.9 application built with Java 17 that integrates the Spring AI Alibaba Agent Framework. The project uses Maven for dependency management and Lombok for boilerplate reduction.

Key dependencies:
- Spring AI Alibaba Agent Framework (1.1.0.0-RC2) - for agent-based AI functionality
- Spring AI Alibaba DashScope starter (1.1.0.0-RC2) - provides ChatModel support for Alibaba's DashScope AI service
- Spring Boot Web - for REST API functionality
- Lombok - for reducing boilerplate code

## Build and Development Commands

### Build the project
```bash
mvn clean install
```

### Run the application
```bash
mvn spring-boot:run
```

### Run tests
```bash
mvn test
```

### Run a single test class
```bash
mvn test -Dtest=AgentLearn4jApplicationTests
```

### Run a specific test method
```bash
mvn test -Dtest=ClassName#methodName
```

### Package the application
```bash
mvn package
```

### Skip tests during build
```bash
mvn clean install -DskipTests
```

## Project Structure

Base package: `com.czf.agentLearn4j.agentLearn4j`

Standard Spring Boot layout:
- `src/main/java/` - Application source code
- `src/main/resources/` - Configuration files, static resources, and templates
- `src/test/java/` - Test source code

## Spring AI Alibaba Agent Framework

This project uses the Spring AI Alibaba Agent Framework, which provides agent-based AI capabilities integrated with Alibaba Cloud's DashScope service. When working with agent functionality:

- Agents are likely defined as Spring beans
- DashScope ChatModel is the underlying AI model provider
- Configuration for DashScope API keys and settings should be added to `application.properties`

## Lombok Usage

The project uses Lombok with annotation processing configured in Maven. When creating new classes:
- Use `@Data`, `@Getter`, `@Setter`, `@Builder`, etc. as appropriate
- Lombok is excluded from the final JAR packaging
- The Maven compiler plugin is configured to process Lombok annotations
