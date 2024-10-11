# POSSIBLE-X EDC Orchestrator

The EDC orchestrator is a Java Spring backend which is responsible for EDC communication orchestration as well as the 
communication with the Fraunhofer catalogue, e.g. for offer creation. The EDC orchestrator is supposed to be used in 
conjunction with an [EDC Connector](https://github.com/eclipse-edc/Connector) in the version v0.4.1 and with the 
[IONOS S3 Extension](https://github.com/Digital-Ecosystems/edc-ionos-s3/) in version v2.2.0.

Following flows are implemented (use [PlantText UML Editor](https://www.planttext.com/) to view): 

Provider flow:
```
@startuml
ConsumerProviderFrontend -> EdcOrchestrator: Create Offer
EdcOrchestrator --> FraunhoferCatalog: Create Data Offer
EdcOrchestrator --> EDC: Create Asset
EdcOrchestrator --> EDC: Create Policy
EdcOrchestrator --> EDC: Create Contract Definition
@enduml
```
Consumer flow:
```
@startuml
ConsumerProviderFrontend -> EdcOrchestrator: Consume Offer
EdcOrchestrator --> EDC: Query Catalog (take first entry)
EdcOrchestrator --> EDC: Initiate Negotiation
EdcOrchestrator --> EDC: Get all Negotiations (wait until state is FINALIZED)
EdcOrchestrator --> EDC: Initiate Transfer
EdcOrchestrator --> EDC: Get Transfer by ID (wait unti state is COMPLETED)
@enduml
```

## Structure
The EDC orchestrator is structured as follows:

```
├── src/main/java/eu/possible_x/edc_orchestrator
│   ├── controller          # external REST API controller
│   ├── entities            # internal data models
│      └── edc              # EDC-related data models
│      └── fh               # Fraunhofer catalogue-related data models
│   ├── service             # internal services for processing data from the controller layer
```

## Build

If you only want to build the project, you can go to the root of the repository and run
```
./gradlew build
```
after which the built jar can be found at `edc-orchestrator/build/libs/edc-orchestrator-x.y.z.jar`

## Run
```
export EDC_XAPIKEY="EDC X-API-Key"
export EDC_MGMTBASEURL="EDC management URL"
export FH_CATALOG_URL="Fraunhofer catalogue URL"
export FH_CATALOG_SECRETKEY="secret key"
```

Through gradle:
```
./gradlew bootRun
```

Alternatively running the jar directly (if built previously):
```
java -jar edc-orchestrator/build/libs/edc-orchestrator-x.y.z.jar
```

Once the service is running, you can access it at http://localhost:8080/ .