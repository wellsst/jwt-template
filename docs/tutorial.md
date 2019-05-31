
## Create project


## Server side

Add deps to server/build.gradle

```
    // https://github.com/jwtk/jjwt
    compile 'io.jsonwebtoken:jjwt-api:0.10.5'
    runtime 'io.jsonwebtoken:jjwt-impl:0.10.5',
            // Uncomment the next line if you want to use RSASSA-PSS (PS256, PS384, PS512) algorithms:
            //'org.bouncycastle:bcprov-jdk15on:1.60',
            'io.jsonwebtoken:jjwt-jackson:0.10.5'
```


Add a key to config
```text
jwtKey = "ODQ2MDc3MmUtMzRkNi00NWIzLWFiM2EtZjI5YjVjYmIwNGNk"
```

But where to get the key value?

Create a utility method

For development I put this as a RootController action:

```groovy
    def index() {

        // Generate a secret key we could use for the JWT
        log.info("Generate secret key...")
        String random = UUID.randomUUID().toString()

        // Store this in config
        String base64Key = DatatypeConverter.printBase64Binary(random.getBytes());
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(base64Key);

        // use this in your app code
        SecretKey secretKey = Keys.hmacShaKeyFor(secretBytes);


        //def keyString = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        log.info(base64Key)
        render base64Key
       // redirect(uri:"/index.html")
    }
```

Create a simple User domain

```text
create-domain-class User
```

Add fields and constraints:

```groovy
    String username
    String loginRequestId
    Date loginRequestedOn
    String token

    static constraints = {
        username email: true, nullable: false, blank: false, unique: true
        token nullable: true
        loginRequestId nullable: true
        loginRequestedOn nullable: true
    }
```

Create supporting services

`create-service Auth`
`create-service Email`

Add the methods as per github


## Controllers

Create a BaseController that will give an auth intercept for any extending controllers

```text
create-controller Base
```

Add methods 


```groovy
def checkPermissions(String token) {
        if (!token) {
            throw new Exception("No token provided")
        }
        log.info "Check permissions: ${token}"
        /*User user = User.findByUsername(token, [cache: true])
        if (!user) {
            throw new Exception("No user found for token: ${token}")
        }
        user */
        authService.loginFromJWT(token)
    }
```

Update UrlMappings.groovy

```groovy
        "/signup" (controller: "login", action: "signup")
        "/login" (controller: "login", action: "login")
        "/signupRequest" (controller: "login", action: "signupRequest")
        "/requestJWT/$requestId" (controller: "login", action: "requestJWT")
        "/loginWithJWT" (controller: "login", action: "loginWithJWT")
```

Views:

Add the loginRequestEmail.gsp its just a template


## Client side

cd client/

// User will enter their email and click request:
ng g c register

ng g c register-email-sent

ng g c request-jwt

ng g c welcome

ng g s auth

npm install moment --save

npm install --save @angular/material @angular/cdk @angular/animations
(https://material.angular.io/guide/getting-started)

- or -

https://www.primefaces.org/primeng/#/

Add the Inteceptors

app.module.ts:

```typescript
import {
  MatFormFieldModule,
...
  MatDialogModule, MatBadgeModule, MatExpansionModule,
  MatSelectModule,
  MatTabsModule

} from '@angular/material';
```

```typescript
providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    AuthGuard,
    AuthService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
}
``` 

```typescript
const appRoutes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'login-email-sent', component: LoginEmailSentComponent},
}

@NgModule({ ...

```


Start all this up locally:

./gradlew bootRun --parallel


GraphQL

ng add apollo-angular

* https://grails.github.io/gorm-graphql/latest/guide/index.html
* https://guides.grails.org/gorm-graphql-with-react-and-apollo/guide/index.html





    
