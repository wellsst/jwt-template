# jwt-template
JWT by example with Grails, Angular and a peppering of GraphML

## Starting up

This profile has created a multi-project build where the "server" module contains the Grails application and the "client" module contains the Angular 6 application.

To start the Grails application:              `./gradlew server:bootRun`
To start the client application:              `./gradlew client:bootRun`
To start both client and Grails applications: `./gradlew bootRun --parallel`

Run the frontend unit tests and Grails unit tests: `./gradlew test`
Run the frontend e2e and grails integration tests: `./gradlew integrationTest`

Node.js/npm is not required when using the Gradle tasks above, but is supported if installed.
E.g., from the `client` directory, start the client application: `npm start`

##  Dependencies

* Java web tokens grails: https://github.com/jwtk/jjwt 
* Angular: https://github.com/auth0/angular2-jwt

## Inspired by

* http://www.devglan.com/spring-security/angular-jwt-authentication
* http://jasonwatmore.com/post/2016/08/16/angular-2-jwt-authentication-example-tutorial
* https://medium.com/@ryanchenkie_40935/angular-authentication-using-the-http-client-and-http-interceptors-2f9d1540eb8
* https://theinfogrid.com/tech/developers/angular/refreshing-authorization-tokens-angular-6/
