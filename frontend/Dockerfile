FROM node:latest AS build
WORKDIR /home/node/src
COPY ./ /home/node/src

WORKDIR /home/node/src/frontend
RUN npm install
RUN npm run build
EXPOSE 4200

ENTRYPOINT ["sh", "-c", "npm start -- --configuration ${ACTIVEPROFILE}"]
