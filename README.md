# jwt-template
JWT by example/starter template with Grails, Angular

## Why JWT?

JSON Web Tokens can provide a simple form of authentication that is an alternative to passwords, 2FA or using an OpenAuth provider.

https://codeburst.io/jwt-to-authenticate-servers-apis-c6e179aa8c4e

Advantages:
* Simplicity
* User does not write down or store or forget yet another password - cumbersome and unsafe* 
* Less vulnerable to keystroke loggers
* Can hold any information, just don't make it sensitive unless using JSON Web Encryption (JWE).
* Flexible and interoperable - JSON based
* Tokens can contain other tokens - a token chain
* JWT can be can be cryptographically signed and encrypted to prevent tampering on the client side
* Server side can verify JWT without needing to store data (typically you would, depending on use-case)

## Current framework versions used

* Grails 3.x
* Angular 8.x 

## Starting up locally

This profile has created a multi-project build where the "server" module contains the Grails application and the "client" module contains the Angular 6 application.

To start the Grails application:              `./gradlew server:bootRun`
To start the client application:              `./gradlew client:bootRun`
To start both client and Grails applications: `./gradlew bootRun --parallel`

Run the frontend unit tests and Grails unit tests: `./gradlew test`
Run the frontend e2e and grails integration tests: `./gradlew integrationTest`

Node.js/npm is not required when using the Gradle tasks above, but is supported if installed.
E.g., from the `client` directory, start the client application: `npm start`

## Deploy (to Heroku)

Follow this: https://github.com/wellsst/diceware

Note: You should enable a secure HTTPS connection, this may help: https://letsencrypt.org/getting-started/

Currently this stores in localStorage this is vulnerable to XSS unless running https (see: https://stormpath.com/blog/where-to-store-your-jwts-cookies-vs-html5-web-storage)
 

Check:
1. Your signing algorithm is strong enough for your purposes
2. The signing key is stored safely, at least needing only a specific user that can read the contents, or store this in a different external location that is locked down and/or encrypted, currently this is config 'app.jwt.key' in application.yml
3. 

Note: Stormpath recommends that you store your JWT in cookies for web applications, because of the additional security they provide, and the simplicity of protecting against CSRF with modern web frameworks. HTML5 Web Storage is vulnerable to XSS, has a larger attack surface area, and can impact all application users on a successful attack.

## Getting started

After startup go to: http://localhost:8080/util, this will generate a random signing key.  Copy and paste this into application.yml
under the app.jwt.key

For this to work in a production environment the whole thing hinges on running over a secure HTTPS connection.  THere are a couple of ways to do this
[Start from the server-side](http://grailsblog.objectcomputing.com/deployment/2017/06/28/running-grails-with-a-self-signed-ssl-certificate.html)
This might help as well: https://docs.rundeck.com/docs/administration/security/configuring-ssl.html

Consider running a local mail server such as https://mailslurper.com or point to your development one by editing application.yml

## Basic 'Happy path'...

### Landing page

A user will see this public unsecured page as an entry point to your app, with a warning if it is not running over a secure connection

![Landing page](https://github.com/wellsst/jwt-template/raw/master/docs/1_index.PNG "Landing page")

### Registration request

User wants to signup with the service

![Reg Request](https://github.com/wellsst/jwt-template/raw/master/docs/2_reg_request.PNG "Reg Request")

### Registration acceptance

Server has sent an email to the user with a unique and expiring link, they are shown a challengeId that they must soon enter

![Reg accept](https://github.com/wellsst/jwt-template/raw/master/docs/3_reg_accept.PNG "Reg accept")

#### The basic email for the next steps

![Email](https://github.com/wellsst/jwt-template/raw/master/docs/4_email.png "Email")

#### User clicks on the link in the email and enters the challengeId

![Enter challenge ID](https://github.com/wellsst/jwt-template/raw/master/docs/5_challengeid.png "Enter challenge ID")

### Registration complete

![Reg complete](https://github.com/wellsst/jwt-template/raw/master/docs/6_complete.png "Reg complete")

On the server you can currently see this via the graphql browser, it looks like:

```json
{
  "data": {
    "userList": [
      {
        "id": 1,
        "username": "test_req@jwt-template.org",
        "loginToken": "eyJhbGciOlJIUzM4NCJ9.eyJpc3MiOiJqd3QsdGVtcGxhdGUiLCJzdWIiOiJ3ZWxsc3N0QDdtYWlsLmNvbSIsImlhdCI6MTU2MDk4Nzg2MiwibmJmIjoxNTYwOTg3ODYyLCJleHAiOjE1OTI1MjM4NjN9.uvMFlPaUzBJY3EgnP5QR-4kG8HZchT7h98pPKUZQ-XJjH86aTnDDtZ6K5k_XOB4b",
        "registrationRequest": {
          "requestId": "2b39117a-ad62-431d-a250-e15308723524",
          "dateCreated": "2019-06-19T23:43:49Z",
          "challengeId": "8886"
        }
      }
    ]
  }
}
```

#### User can now view the previously guarded page

![View guarded page](https://github.com/wellsst/jwt-template/raw/master/docs/7_guarded.png "View guarded page")

## TODO

* Improve unit tests on client and server
* Add admin type/util features such as list users token, expire tokens
* User login history
* Lockdown the grails graphQL service

##  Dependencies

* Java web tokens grails: https://github.com/jwtk/jjwt 
* Angular: https://github.com/auth0/angular2-jwt
* http://gpc.github.io/grails-mail/

### GraphQL
* https://grails.github.io/gorm-graphql/latest/guide/index.html
* https://guides.grails.org/gorm-graphql-with-react-and-apollo/guide/index.html
* https://www.apollographql.com/docs/tutorial/queries/

## References and inspiration

* [Where to Store your JWTs – Cookies vs HTML5 Web Storage](https://stormpath.com/blog/where-to-store-your-jwts-cookies-vs-html5-web-storage)
* [http://www.devglan.com/spring-security/angular-jwt-authentication](http://www.devglan.com/spring-security/angular-jwt-authentication)
* [https://jasonwatmore.com/post/2018/11/16/angular-7-jwt-authentication-example-tutorial](https://jasonwatmore.com/post/2018/11/16/angular-7-jwt-authentication-example-tutorial)
* [https://medium.com/@ryanchenkie_40935/angular-authentication-using-the-http-client-and-http-interceptors-2f9d1540eb8](https://medium.com/@ryanchenkie_40935/angular-authentication-using-the-http-client-and-http-interceptors-2f9d1540eb8)
* [https://theinfogrid.com/tech/developers/angular/refreshing-authorization-tokens-angular-6/](https://theinfogrid.com/tech/developers/angular/refreshing-authorization-tokens-angular-6/)




