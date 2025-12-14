
FROM eclipse-temurin:17-jdk-alpine 
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
COPY target/*.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]


# Local Development
#docker run --name mysql-loan -e MYSQL_ROOT_PASSWORD=9787 -e MYSQL_DATABASE=loanmanagement -p 3306:3306 -d mysql:8.0
#mvn spring-boot:run

# Docker (Everything)
#mvn clean package -DskipTests
#docker-compose up -d
#docker-compose logs -f

# Stop Everything
#docker-compose down

# Clean Everything (including data)
#docker-compose down -v