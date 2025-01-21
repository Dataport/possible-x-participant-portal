FROM gradle:8.9.0-jdk17 AS build
ARG ACTIVEPROFILE

# install chrome headless
RUN apt-get update && \
  apt-get -y upgrade && \
  apt-get install -yq curl libgconf-2-4 gnupg wget
RUN curl https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb stable main' >> /etc/apt/sources.list.d/google-chrome.list
RUN apt-get update && apt-get install -y  --no-install-recommends google-chrome-stable fonts-ipafont-gothic fonts-wqy-zenhei fonts-thai-tlwg fonts-kacst

WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle buildFrontend --no-daemon -DactiveProfile=${ACTIVEPROFILE}

FROM nginx:stable-alpine
COPY --from=build /home/gradle/src/build/resources /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 8080
