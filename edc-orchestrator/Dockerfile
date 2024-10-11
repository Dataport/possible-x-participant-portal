FROM gradle:8.9.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/edc-orchestrator/build/libs/*.jar /app/edc-orchestrator.jar

ENTRYPOINT ["java","-jar","/app/edc-orchestrator.jar"]