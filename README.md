# POSSIBLE-X Participant Portal

This repository contains a graphical user interface that is supposed to be used in conjunction with
an [EDC Connector](https://github.com/eclipse-edc/Connector) in the version v0.4.1 and with
the [IONOS S3 Extension](https://github.com/Digital-Ecosystems/edc-ionos-s3/) in version v2.2.0.

It aims to provide business-centric functionality for creating and providing service offers as well as consuming
existing service offers found in a catalogue.

## Repository structure

The repository is structured as a Gradle multi-project build.

```
(...)
├── libs.versions.toml          # configuration file of version catalog for dependencies
├── settings.gradle.kts         # root project settings
├── buildSrc/                   # shared build configuration
├── frontend/                   # Angular frontend code for the GUI
│   └── build.gradle.kts        # build file for the Angular frontend
├── backend/                    # Spring backend code for EDC communication
│   └── build.gradle.kts        # build file for the Spring backend
```

## (Re-)Generate Typescript API interfaces and REST client

The typescript API interfaces and the corresponding REST client are auto-generated from the Spring backend entity and
controller classes using the following command:

```
./gradlew generateTypeScript
```

Afterwards they can be found at

```
frontend/src/app/services/mgnt/api/backend.ts
```

## Build

If you only want to build the project, you can run

```
./gradlew build
```

after which the built jar can be found at `backend/build/libs/backend-x.y.z.jar`

## Run Participant Portal Backend (currently including frontend)

Through gradle:

```
./gradlew bootRun
```

Running a specific configuration:

E.g. for local consumer:

```
./gradlew bootRun --args='--spring.profiles.active=consumer-local' -PnpmEnv="consumer-local"
```

E.g. for local provider:

```
./gradlew bootRun --args='--spring.profiles.active=provider-local' -PnpmEnv="provider-local"
```

Alternatively running the jar directly (if built previously):

```
java -jar backend/build/libs/backend-x.y.z.jar
```

Once the service is running, you can access it at e.g. http://localhost:8080/ (depending on the used configuration).

The OpenAPI documentation can be found at http://localhost:8080/swagger-ui.html .

## Run Participant Portal Frontend

Consumer (local testing):

```
cd frontend/
npm run ng -- serve --configuration consumer-local --port 4201
```

Provider (local testing):

```
cd frontend/
npm run ng -- serve --configuration provider-local --port 4200
```

Once the service is running, you can access it at e.g. http://localhost:4200/  (depending on the used configuration).
