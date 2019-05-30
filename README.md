# jwt-template
JWT by example/starter template with Grails, Angular

## Starting up

This profile has created a multi-project build where the "server" module contains the Grails application and the "client" module contains the Angular 6 application.

To start the Grails application:              `./gradlew server:bootRun`
To start the client application:              `./gradlew client:bootRun`
To start both client and Grails applications: `./gradlew bootRun --parallel`

Run the frontend unit tests and Grails unit tests: `./gradlew test`
Run the frontend e2e and grails integration tests: `./gradlew integrationTest`

Node.js/npm is not required when using the Gradle tasks above, but is supported if installed.
E.g., from the `client` directory, start the client application: `npm start`

## Getting started

After startup go to: http://localhost:8080/root, this will generate a random signing key.  Copy and paste this into application.yml
under the app.jwt.key

## TODO

* Implement the challengeId - store it as a simple hash
* Client side error handling
* Unit tests
* Add admin type/util features such as list users token, expire tokens

##  Dependencies

* Java web tokens grails: https://github.com/jwtk/jjwt 
* Angular: https://github.com/auth0/angular2-jwt

## Inspired by

* http://www.devglan.com/spring-security/angular-jwt-authentication
* https://jasonwatmore.com/post/2018/11/16/angular-7-jwt-authentication-example-tutorial
* https://medium.com/@ryanchenkie_40935/angular-authentication-using-the-http-client-and-http-interceptors-2f9d1540eb8
* https://theinfogrid.com/tech/developers/angular/refreshing-authorization-tokens-angular-6/
