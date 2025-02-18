# POSSIBLE-X Participant Portal Frontend

The frontend component of the POSSIBLE-X Participant Portal is an Angular application that provides the GUI for the user to interact with the backend.
It provides a mask for consuming Service Offerings / Data Service Offerings from the central POSSIBLE-X Catalogue, another mask for providing Service
Offerings / Data Service Offerings to the central POSSIBLE-X Catalogue, and a view where the user can look up the contracts they have closed in the
POSSIBLE-X Dataspace.

The frontend itself does not contain major logic or data processing, it depends on the backend component for that.

The frontend uses an adapted version of the [SD Creation Wizard](https://gitlab.eclipse.org/eclipse/xfsc/self-description-tooling/sd-creation-wizard-frontend) to create parts of the
mask where participants can fill out all the fields needed to provide offerings.
The SD Creation Wizard is integrated as a component and extended based on the implementation of the
[MERLOT Marketplace Frontend](https://github.com/merlot-education/marketplace-frontend).

## Structure

```
├── src/
│   ├── app
│   │   ├── containers        # shared layout throughout all frontend components
│   │   ├── interceptors      # interceptors, e.g. for adding authorization headers to requests
│   │   ├── sdwizard          # self-description creation wizard based on XFSC Self-Description Wizard Frontend
│   │   ├── services          # shared services, e.g. for interacting with the backend
│   │   ├── views             # frontend components for all GUI functionality 
│   │   ├── utils             # shared static functionality 
│   │   └── wizard-extension  # POSSIBLE-X specific extension of the aforementioned self-description creation wizard
│   ├── assets                # static assets like images
│   ├── environments          # environment-specific configurations
│   └── styles                # global styles
```

## Build

If you only want to build the frontend, you can run

```
./gradlew buildFrontend
```

after which the built frontend can be found at `frontend/build/resources/`.

## Run

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

## Run unit tests

Execute the unit tests via [Karma](https://karma-runner.github.io) with npm:

```
npm --prefix frontend/ test
```

**Note:** On some systems, e.g. when you are using WSL for development, you might need to set the chrome binary to `CHROME_BIN=/bin/chromium-browser` to run the frontend tests individually.
