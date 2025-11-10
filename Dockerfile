# Stage 1: Build Stage   # this is more optimize version
FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .
COPY src ./src

# skip testing unit # Then copy source and build
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Expose port running in container
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]