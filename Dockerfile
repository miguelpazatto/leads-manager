FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY target/generated-sources/annotations .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]