FROM gradle:8.9.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/backend/build/libs/*.jar /app/backend.jar

ARG VERSION_NO="unknown"
ENV VERSION_NO=$VERSION_NO
ENTRYPOINT ["java","-jar","/app/backend.jar"]