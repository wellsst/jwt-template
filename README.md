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

After startup go to: http://localhost:8080/util, this will generate a random signing key.  Copy and paste this into application.yml
under the app.jwt.key

For this to work in a production environment the whole thing hinges on running over a secure HTTPS connection.  THere are a couple of ways to do this
[Start from the server-side](http://grailsblog.objectcomputing.com/deployment/2017/06/28/running-grails-with-a-self-signed-ssl-certificate.html)
This might help as well: https://docs.rundeck.com/docs/administration/security/configuring-ssl.html

Consider running a local mail server such as https://mailslurper.com or point to your development one by editing application.yml

## The flow of the Demo

![Landing page](https://github.com/wellsst/jwt-template/raw/master/docs/1_index.PNG "Landing page")
![Reg Request](https://github.com/wellsst/jwt-template/raw/master/docs/2_reg_request.PNG "Reg Request")

![Reg accept](https://github.com/wellsst/jwt-template/raw/master/docs/3_reg_accept.PNG "Reg accept")

![Email](https://github.com/wellsst/jwt-template/raw/master/docs/4_email.png "Email")

![Enter challenge ID](https://github.com/wellsst/jwt-template/raw/master/docs/5_challengeid.png "Enter challenge ID")

![Reg complete](https://github.com/wellsst/jwt-template/raw/master/docs/6_complete.png "Reg complete")

![View guarded page](https://github.com/wellsst/jwt-template/raw/master/docs/7_guarded.png "View guarded page")

## TODO

* Improve unit tests on client and server
* Add admin type/util features such as list users token, expire tokens
* User login history

##  Dependencies

* Java web tokens grails: https://github.com/jwtk/jjwt 
* Angular: https://github.com/auth0/angular2-jwt
* http://gpc.github.io/grails-mail/

### GraphQL
* https://grails.github.io/gorm-graphql/latest/guide/index.html
* https://guides.grails.org/gorm-graphql-with-react-and-apollo/guide/index.html
* https://www.apollographql.com/docs/tutorial/queries/

## Inspired by

* http://www.devglan.com/spring-security/angular-jwt-authentication
* https://jasonwatmore.com/post/2018/11/16/angular-7-jwt-authentication-example-tutorial
* https://medium.com/@ryanchenkie_40935/angular-authentication-using-the-http-client-and-http-interceptors-2f9d1540eb8
* https://theinfogrid.com/tech/developers/angular/refreshing-authorization-tokens-angular-6/

## Flow:



