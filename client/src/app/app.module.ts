import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {IndexComponent} from './index/index.component';
import {AppComponent} from './app.component';
import {NavComponent} from './nav/nav.component';
import {NavService} from './nav/nav.service';
import {AppRoutingModule} from "./app-routing.module";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RequestJWTComponent} from './request-jwt/request-jwt.component';
import {ErrorInterceptor} from "./ErrorInterceptor";
import {AuthInterceptor} from "./AuthInterceptor";
import {AuthService} from "./auth.service";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AuthGuard} from "./guards/auth.guard";
import {WelcomeComponent} from './welcome/welcome.component';
import {RouterModule, Routes} from "@angular/router";
import { RegisterComponent } from './register/register.component';
import { RegisterEmailSentComponent } from './register-email-sent/register-email-sent.component';

const appRoutes: Routes = [
  {path: '', component: WelcomeComponent, canActivate: [AuthGuard]},
  {path: 'register', component: RegisterComponent},
  {path: 'request-jwt', component: RequestJWTComponent},
  {path: 'register-email-sent', component: RegisterEmailSentComponent},
  {path: '**', redirectTo: ''}
];


@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    IndexComponent,
    RequestJWTComponent,
    WelcomeComponent,
    RegisterComponent,
    RegisterEmailSentComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule.forRoot(),
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: true} // <-- debugging purposes only
    ),
  ],
  providers: [
    NavService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
