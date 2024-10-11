# POSSIBLE-X EDC Management GUI

This repository contains a graphical user interface that is supposed to be used in conjunction with an [EDC Connector](https://github.com/eclipse-edc/Connector) in the version v0.4.1 and with the [IONOS S3 Extension](https://github.com/Digital-Ecosystems/edc-ionos-s3/) in version v2.2.0.

It aims to provide business-centric functionality for creating and providing service offers as well as consuming existing service offers found in a catalogue.

## Repository structure
The repository is structured as a Gradle multi-project build.

```
(...)
├── libs.versions.toml          # configuration file of version catalog for dependencies
├── settings.gradle.kts         # root project settings
├── buildSrc/                   # shared build configuration
├── consumer-provider-frontend/ # Angular frontend code for the GUI
│   └── build.gradle.kts        # build file for the Angular frontend
├── edc-orchestrator/           # Spring backend code for EDC communication orchestration
│   └── build.gradle.kts        # build file for the Spring backend
```

## Build

If you only want to build the project, you can run
```
./gradlew build
```
after which the built jar can be found at `edc-orchestrator/build/libs/edc-orchestrator-x.y.z.jar`

## Run EDC Orchestrator
Through gradle:
```
./gradlew bootRun
```

Running a specific configuration (e.g. for local consumer)
```
./gradlew bootRun --args='--spring.profiles.active=consumer-local'
```

Alternatively running the jar directly (if built previously):
```
java -jar edc-orchestrator/build/libs/edc-orchestrator-x.y.z.jar
```

Once the service is running, you can access it at e.g. http://localhost:8080/ (depending on the used configuration).

The OpenAPI documentation can be found at http://localhost:8080/swagger-ui.html .

## Run Consumer/Provider Frontend
Consumer (local testing):
```
cd consumer-provider-frontend/
npm run ng -- serve --configuration consumer-local --port 4201
```

Provider (local testing):
```
cd consumer-provider-frontend/
npm run ng -- serve --configuration provider-local --port 4200
```

Once the service is running, you can access it at e.g. http://localhost:4200/  (depending on the used configuration).
