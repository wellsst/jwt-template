import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../environments/environment';
// import 'rxjs-compat/add/operator/map';
// import moment = require('moment');
import * as moment from 'moment';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

/**
 * Derived from http://jasonwatmore.com/post/2016/08/16/angular-2-jwt-authentication-example-tutorial
 * Updated for angular 7: https://jasonwatmore.com/post/2018/11/16/angular-7-jwt-authentication-example-tutorial#auth-guard-ts
 */
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public token: string;
  public username: string;
  public error: string;

  constructor(private http: HttpClient) {
    // set token if saved in local storage
    /*var currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.token = currentUser && currentUser.token;
    this.username = currentUser && currentUser.username;*/
  }


  signupRequest(emailAddress: string): Observable<Object> {
    return this.http.post(environment.serverUrl + 'signupRequest/',
      JSON.stringify({emailAddress: emailAddress}), httpOptions); // todo: should not need httpOptions here use the inteceptor
  }

  /* requestJWT(loginId: string) {
     return this.http.post(environment.serverUrl + '/requestJWT/' + loginId,
       {loginId: loginId}, httpOptions).do(res => this.setSession);
   }
 */
  login(jwtToken: string) {
    this.setSession(jwtToken);
    /*return this.http.post(environment.serverUrl + '/login/',
      {username: username, jwtToken: jwtToken}, httpOptions).do(res => this.setSession).shareReplay();*/

  }

  // So JWT can handle UTF-8: https://stackoverflow.com/questions/30106476/using-javascripts-atob-to-decode-base64-doesnt-properly-decode-utf-8-strings
  private b64DecodeUnicode(str) {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(atob(str).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
  }

  private parseJwt(token) {
    try {
      // Get Token Header
      const base64HeaderUrl = token.split('.')[0];
      const base64Header = base64HeaderUrl.replace('-', '+').replace('_', '/');
      const headerData = JSON.parse(this.b64DecodeUnicode(base64Header));

      // Get Token payload and date's
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace('-', '+').replace('_', '/');
      const dataJWT = JSON.parse(this.b64DecodeUnicode(base64));
      dataJWT.header = headerData;

      // TODO: add expiration at check ...

      return dataJWT;
    } catch (err) {
      return false;
    }
  }

  private setSession(jwtToken) {
    const jwtDecoded = this.parseJwt(jwtToken);
    if (jwtDecoded) {
      console.log(jwtDecoded);
    }

    // const expiresAt = moment().add(jwtToken.expiresIn, 'second');
    localStorage.setItem('id_token', jwtToken);
    localStorage.setItem('expires_at', JSON.stringify(new Date(jwtDecoded.exp * 1000)));
    localStorage.setItem('issued_on', JSON.stringify(new Date(jwtDecoded.iat * 1000)));
    localStorage.setItem('subject', JSON.stringify(jwtDecoded.sub));
  }

  logout() {
    localStorage.removeItem('id_token');
    localStorage.removeItem('expires_at');
    localStorage.removeItem('issued_on');
    localStorage.removeItem('subject');
  }

  public isLoggedIn() {
    // todo: Could check not before as well
    return moment().isBefore(this.getExpiration());
  }

  isLoggedOut() {
    return !this.isLoggedIn();
  }

  getExpiration() {
    const expiration = localStorage.getItem('expires_at');
    const expiresAt = JSON.parse(expiration);
    return moment(expiresAt);
  }


  /*login(username: string, password: string): Observable<boolean> {
    return this.http.post(environment.serverUrl + '/login/',
      JSON.stringify({username: username, password: password}), httpOptions).map((response: any) => {
      // login successful if there's a jwt token in the response
      let token = response && response.token;

      if (token) {
        // set token property
        this.token = token;
        this.username = username;

        // store username and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('currentUser', JSON.stringify({username: username, token: token}));

        // return true to indicate successful login
        return true;
      } else {
        // return false to indicate failed login
        return false;
      }
    });
  }

  signup(emailAddress: string): Observable<boolean> {
    return this.http.post(environment.serverUrl + '/signup',
      JSON.stringify({emailAddress: emailAddress}), httpOptions).map((response: any) => {
      // login successful if there's a jwt token in the response
      let token = response && response.token;

      if (token) {
        // set token property
        this.token = token;
        this.username = emailAddress;

        // store username and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('currentUser', JSON.stringify({username: emailAddress, token: token}));

        // return true to indicate successful login
        return true;
      } else {
        // return false to indicate failed login
        this.error = response.text;
        return false;
      }
    });
  }

  logout(): void {
    // clear token remove user from local storage to log user out
    this.token = null;
    localStorage.removeItem('currentUser');
  }

  isLoggedIn(): boolean {
    return this.token != null;
  }*/
}
