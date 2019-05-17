import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {LocationStrategy, HashLocationStrategy} from '@angular/common';
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
import {LoginComponent} from './login/login.component';

import {
  MatFormFieldModule,
  MatDatepickerModule, MatNativeDateModule,
  MatInputModule,
  MatToolbarModule,
  MatSidenavModule,
  MatListModule,
  MatGridListModule,
  MatCardModule,
  MatMenuModule,
  MatIconModule,
  MatButtonModule,
  MatTableModule,
  MatPaginatorModule,
  MatSortModule,
  MatAutocompleteModule,
  MatSliderModule,
  MatChipsModule,
  MatSnackBarModule,
  MatCheckboxModule,
  MatProgressBarModule,
  MatDialogModule, MatBadgeModule, MatExpansionModule,
  MatSelectModule,
  MatTabsModule

} from '@angular/material';
import { LoginEmailSentComponent } from './login-email-sent/login-email-sent.component';
import { WelcomeComponent } from './welcome/welcome.component';
import {Routes} from "@angular/router";

const appRoutes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'login-email-sent', component: LoginEmailSentComponent}
  ]


  @NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    IndexComponent,
    RequestJWTComponent,
    LoginComponent,
    LoginEmailSentComponent,
    WelcomeComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule.forRoot(),

    // Overkill
    MatGridListModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule, MatNativeDateModule,
    MatSliderModule,
    MatSnackBarModule,
    MatChipsModule,
    MatCheckboxModule,
    MatProgressBarModule,
    MatDialogModule,
    MatIconModule,
    MatBadgeModule,
    MatExpansionModule,
    MatSelectModule,
    MatTabsModule
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    NavService,
    AuthGuard,
    AuthService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
    ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
