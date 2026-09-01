# Stage 1: Build
FROM maven:3.9.16-amazoncorretto-25 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests


# Stage 2: Run
FROM amazoncorretto:25

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]