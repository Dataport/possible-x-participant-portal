# POSSIBLE-X Participant Portal Frontend

The POSSIBLE-X Participant Portal is the decentralized UI for the participants in the Portal-X dataspace who want to consume or provide offerings.

It consists of an Angular frontend and a Spring-Boot backend.

Participants can use the frontend to consume and provide offerings. The frontend itself does not contain major logic or data processing, it depends on the backend service for that.

The frontend uses an adapted version of the [SD Creation Wizard](https://gitlab.eclipse.org/eclipse/xfsc/self-description-tooling/sd-creation-wizard-frontend) to create parts of the
mask where participants can fill out all the fields needed to provide offerings. The SD Creation Wizard is integrated as a component and extended based on the implementation of the
[MERLOT Marketplace Frontend](https://github.com/merlot-education/marketplace-frontend).

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
