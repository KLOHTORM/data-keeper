FROM gradle:jdk21 AS builder

WORKDIR /builder

COPY ./src ./src
COPY ./build.gradle .
COPY ./settings.gradle .
COPY ./gradle ./gradle

RUN gradle build

FROM openjdk:21-jdk-slim

WORKDIR /opt/app

# Copying built jars from builder
COPY --from=builder /builder/build/libs/*-SNAPSHOT.jar app.jar

CMD ["java", "-Dspring.profiles.active=debug", "-jar", "app.jar"]
EXPOSE 8080