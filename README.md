# POSSIBLE-X Participant Portal

The POSSIBLE-X Participant Portal is the decentralized UI for the participants in the POSSIBLE-X Dataspace who want to
consume or provide Service Offerings / Data Service Offerings in the central POSSIBLE-X Catalogue / Fraunhofer (FH)
Catalog. It allows the participants to look up the contracts they have closed in the POSSIBLE-X Dataspace as well.

The POSSIBLE-X Participant Portal consists of an Angular frontend and a Spring Boot backend. The backend is supposed to
be used in conjunction with an [EDC Connector](https://github.com/eclipse-edc/Connector) in the version `v0.4.1` and with
the [IONOS S3 Extension](https://github.com/Digital-Ecosystems/edc-ionos-s3/) in version `v2.3.1`.

## Repository structure

The repository is structured as a Gradle multi-project build.

```
(...)
├── libs.versions.toml          # configuration file of version catalog for dependencies
├── settings.gradle.kts         # root project settings
├── build.gradle.kts            # root build file
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

Afterward they can be found at

```
frontend/src/app/services/mgnt/api/backend.ts
```

## Build Backend

If you only want to build the backend, you can run

```
./gradlew buildBackend
```

after which the built jar can be found at `backend/build/libs/backend-x.y.z.jar`

## Run Backend

Through gradle:

```
./gradlew startBackend
```

Running a specific configuration:

E.g. for local consumer:

```
./gradlew startBackend -PactiveProfile=consumer-local
```

E.g. for local provider:

```
./gradlew startBackend -PactiveProfile=provider-local
```

Alternatively running the jar directly (if built previously):

```
java -jar backend/build/libs/backend-x.y.z.jar --spring.profiles.active=provider-local
```

Once the service is running, you can access it at e.g. http://localhost:8080/ (depending on the used configuration).

The OpenAPI documentation can be found at http://localhost:8080/swagger-ui.html .

## Build Frontend

If you only want to build the frontend, you can run

```
./gradlew buildFrontend
```

after which the built frontend can be found at `frontend/build/resources/`.

## Run Frontend

Through gradle:

```
./gradlew startFrontend
```

Running a specific configuration:

E.g. for local consumer:

```
./gradlew startFrontend -PactiveProfile=consumer-local
```

E.g. for local provider:

```
./gradlew startFrontend -PactiveProfile=provider-local
```

Alternatively running with npm directly:

```
npm --prefix frontend/ run ng -- serve --configuration provider-local --port 4200
```

Once the service is running, you can access it at e.g. http://localhost:4200/ (depending on the used configuration).

## Run Full Application (Frontend and Backend)

In addition to running the frontend and backend individually, there is also a gradle task for running both in parallel.
Note that when the app is started through this task, the IntelliJ debugger will not be able to attach to the backend and
hence won't stop at breakpoints.
Through gradle:

```
./gradlew startFull
```

Running a specific configuration:

E.g. for local consumer:

```
./gradlew startFull -PactiveProfile=consumer-local
```

E.g. for local provider:

```
./gradlew startFull -PactiveProfile=provider-local
```

## Killing orphaned processes

If for any reason the application is not shut down properly, the following command can be used to kill a service that is
running on the specified port:

```
sudo fuser -k 8080/tcp
```

where 8080 can be replaced with any other port.

## Functional Documentation

The consumer should be able to just copy the offer ID from the FH Catalog offer which he chooses in the FH Catalog.
For that to work, the provider will put infos into the FH Catalog offer when the provider publishes an offer. These
additional infos are the infos which are needed to identify the offer in the EDC Catalog (asset-ID and the URL of the
provider EDC connector).

On the consumer side the user will paste the offer ID into the provider frontend. Then the backend will fetch and parse
the FH offer and parse it for the infos needed to identify the offer in the EDC Catalog.


