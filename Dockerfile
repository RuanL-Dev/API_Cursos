FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
EXPOSE 8080

COPY --from=build /app/target/api_cursos-0.0.1.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]