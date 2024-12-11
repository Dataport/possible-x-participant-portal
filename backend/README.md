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

The following environment variables can be set as needed. 
Replace the right hand side with the actual values.
```
export EDC_XAPIKEY="EDC X-API-Key"
export EDC_MGMTBASEURL="EDC management URL"
export FH_CATALOG_URL="Fraunhofer catalogue URL"
export FH_CATALOG_SECRETKEY="secret key"
export SDCREATIONWIZARDAPI_BASEURL="SD Creation Wizard API base URL"
export PARTICIPANTID="participant ID"
export PREFILLFIELDS_DATAPRODUCT_JSONFILEPATH="File Path"
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

## Environment Variables
### PREFILLFIELDS_DATAPRODUCT_JSONFILEPATH
This environment variable should be set to the path of a JSON file containing text for prefilling fields for a 
data product. The intention is to have an easy way to configure prefill texts for different use cases.

Currently, the JSON file should contain the following fields:
```
{
  "serviceOfferingName": "Title",
  "serviceOfferingDescription": "Description"
}
```
It is possible to add a placeholder `<Data resource name>` in the `serviceOfferingName` and `serviceOfferingDescription` 
fields that would be replaced with the data resource name in the frontend.

If no file path is provided or the file cannot be found, a default JSON file with default values is used.