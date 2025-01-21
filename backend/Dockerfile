FROM gradle:8.9.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildBackend --no-daemon

FROM openjdk:17-jdk-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/backend/build/libs/*.jar /app/backend.jar

ARG VERSION_NO="unknown"
ENV VERSION_NO=$VERSION_NO
ARG VERSION_DATE="unknown"
ENV VERSION_DATE=$VERSION_DATE
ENTRYPOINT ["java","-jar","/app/backend.jar"]
