# POSSIBLE-X Participant Portal Backend

The POSSIBLE-X Participant Portal is the decentralized UI for the participants in the Portal-X dataspace who want to
consume or provide offerings.

It consists of an Angular frontend and a Spring-Boot backend.

The backend manages the EDCs and the offerings in the Fraunhofer Catalog.
The Participant Portal is supposed to be used in conjunction with
an [EDC Connector](https://github.com/eclipse-edc/Connector) in the version `v0.4.1` and with the
[IONOS S3 Extension](https://github.com/Digital-Ecosystems/edc-ionos-s3/) in version `v2.2.0`.

## Build

If you only want to build the project, you can go to the root of the repository and run

```
./gradlew build
```

after which the built jar can be found at `backend/build/libs/backend-x.y.z.jar`.

## Run

```
export EDC_XAPIKEY="EDC X-API-Key"
export EDC_MGMTBASEURL="EDC management URL"
export FH_CATALOG_URL="Fraunhofer catalogue URL"
export FH_CATALOG_SECRETKEY="secret key"
export SDCREATIONWIZARDAPI_BASEURL="SD Creation Wizard API base URL"
export PARTICIPANTID="participant ID"
```

Through gradle:

```
./gradlew bootRun
```

Alternatively running the jar directly (if built previously):

```
java -jar backend/build/libs/backend-x.y.z.jar
```

Once the service is running, you can access it at http://localhost:8080/ .