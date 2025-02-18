# POSSIBLE-X Participant Portal Backend

The backend component of the POSSIBLE-X Participant Portal is a Spring Boot application that provides the REST API for
the frontend. It manages interactions with the participant's EDC and the central POSSIBLE-X Catalogue / Fraunhofer (FH)
Catalog to consume and provide Service Offerings / Data Service Offerings. It also allows looking up the contracts that
the user has closed in the POSSIBLE-X Dataspace.

The Participant Portal backend is supposed to be used in conjunction with
an [EDC Connector](https://github.com/eclipse-edc/Connector) in the version `v0.4.1` and with
the [IONOS S3 Extension](https://github.com/Digital-Ecosystems/edc-ionos-s3/) in version `v2.3.1`.

## Structure

```
├── src/main/java/eu/possiblex/participantportal
│   ├── application         # application layer
│   │   ├── boundary        # external REST API controllers
│   │   ├── configuration   # configuration-related components
│   │   ├── control         # internal services for processing data such as mappers
│   │   └── entity          # application data models
│   ├── business            # business logic layer
│   │   ├── control         # business logic services
│   │   └── entity          # business logic data models
│   ├── utilities           # shared static functionality
```

## Configuration

For a full list of configuration options (including Spring options) please see the
[application.yml](src/main/resources/application.yml).

| Key                                                 | Description                                                                                                                                | Default                                                    |
|-----------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------|
| participant-id                                      | Sets the participant id for the participant portal                                                                                         | "did:web:example-organization.eu"                          |
| sd-creation-wizard-api.base-url                     | Sets the SD Creation Wizard API Service url                                                                                                | http://localhost:8085                                      |
| spring.security.admin.username                      | Sets the admin username for the participant portal                                                                                         | admin                                                      |
| spring.security.admin.password                      | Sets the admin password for the participant portal                                                                                         | admin                                                      |
| daps-server.base-url                                | Sets the DAPS server base url                                                                                                              | http://localhost:4567                                      |
| edc.x-api-key                                       | Sets the X-API-Key for interactions with the EDC                                                                                           | password                                                   |
| edc.mgmt-base-url                                   | Sets the management base url for interactions with the EDC                                                                                 | http://localhost:19193/management                          |
| edc.protocol-base-url                               | Sets the EDC protocol base url                                                                                                             | http://localhost:19194/protocol                            |
| s3.bucket-storage-region                            | Sets the IONOS S3 Bucket storage region                                                                                                    | "eu-central-3"                                             |
| s3.bucket-top-level-folder                          | Sets the IONOS S3 Bucket top level folder                                                                                                  | "possiblex_data_transfers"                                 |
| s3.bucket-name                                      | Sets the IONOS S3 Bucket name                                                                                                              | "dev-provider-edc-bucket-possible-31952746"                |
| fh.catalog.repo.url                                 | Sets the FH Catalog repo url for interactions with the piveau-hub-repo                                                                     | http://localhost:5081/                                     |
| fh.catalog.sparql.url                               | Sets the FH Catalog sparql url for interactions with the SPARQL interface of Virtuoso                                                      | http://localhost:8890/sparql/                              |
| fh.catalog.uri-resource-base                        | Sets the FH Catalog resource base where, e.g. Service Offerings can be retrieved                                                           | http://localhost:5081/resources/                           |
| fh.catalog.secret-key                               | Sets the secret key for API interactions with the FH Catalog                                                                               | yourRepoApiKey                                             |
| prefill-fields.data-service-offering.json-file-path | Sets the path to a JSON file for prefilling Data Service Offering fields, see [below](#prefill-fieldsdata-service-offeringjson-file-path). | "src/main/resources/prefillFieldsDataServiceOffering.json" |
| version.no                                          | Sets the version number of the portal which is shown in the POSSIBLE-X Participant Portal UI                                               | "1.0.0"                                                    |
| version.date                                        | Sets the version date of the portal which is shown in the POSSIBLE-X Participant Portal UI                                                 | "2024-12-31"                                               |

### prefill-fields.data-service-offering.json-file-path

Provide the path to a JSON file for prefilling data service offering fields. This allows easy configuration of prefill
texts for different use cases.

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

## Build

If you only want to build the project, you can go to the root of the repository and run

```
./gradlew buildBackend
```

after which the built jar can be found at `backend/build/libs/backend-x.y.z.jar`.

## Run

The configuration options mentioned above can be set via environment variables as needed.
Below are some examples, please replace the right hand side with the actual values.

```
export EDC_XAPIKEY="EDC X-API-Key"
export EDC_MGMTBASEURL="EDC management URL"
export FH_CATALOG_REPO_URL="Fraunhofer catalogue URL to piveau-hub-repo"
export FH_CATALOG_SPARQL_URL="Fraunhofer catalogue URL to the SPARQL interface of Virtuoso"
export FH_CATALOG_SECRETKEY="secret key"
export SDCREATIONWIZARDAPI_BASEURL="SD Creation Wizard API base URL"
export PARTICIPANTID="participant ID"
export PREFILLFIELDS_DATASERVICEOFFERING_JSONFILEPATH="File Path"
```

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
java -jar backend/build/libs/backend-x.y.z.jar
```

Or running the jar with a specific configuration, e.g. for local provider:

```
java -jar backend/build/libs/backend-x.y.z.jar --spring.profiles.active=provider-local
```

Once the service is running, you can access it at e.g. http://localhost:8080/ (depending on the used configuration).

The OpenAPI documentation can be found at http://localhost:8080/swagger-ui.html .