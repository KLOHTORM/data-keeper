FROM gradle:jdk21 AS builder

WORKDIR /builder

COPY ./src .
COPY ./build.gradle .
COPY ./settings.gradle .
COPY ./gradle .

RUN gradle build



FROM azul/zulu-openjdk-debian:21-jre-latest

WORKDIR /opt/app

COPY --from=builder /builder/build/libs/data-keeper-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080
