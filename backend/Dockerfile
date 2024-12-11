FROM gradle:8.9.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
RUN apt-get update && \
  apt-get -y upgrade && \
  apt-get install -yq curl libgconf-2-4 gnupg wget
RUN curl https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb stable main' >> /etc/apt/sources.list.d/google-chrome.list
RUN apt-get update && apt-get install -y  --no-install-recommends google-chrome-stable fonts-ipafont-gothic fonts-wqy-zenhei fonts-thai-tlwg fonts-kacst
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/backend/build/libs/*.jar /app/backend.jar

ARG VERSION_NO="unknown"
ENV VERSION_NO=$VERSION_NO
ARG VERSION_DATE="unknown"
ENV VERSION_DATE=$VERSION_DATE
ENTRYPOINT ["java","-jar","/app/backend.jar"]