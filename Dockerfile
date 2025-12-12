# Stage 1: Build Stage   # this is more optimize version
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .
COPY src ./src

# skip testing unit # Then copy source and build
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

# Expose port running in container
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]