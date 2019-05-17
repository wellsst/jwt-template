import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {AuthService} from './auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthService) {}

  // could enahnce with logging like: https://theinfogrid.com/tech/developers/angular/logging-http-errors-in-angular-6
  // token handling: https://theinfogrid.com/tech/developers/angular/refreshing-authorization-tokens-angular-6/
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      if (err.status === 401 || err.status === 403) {
        // auto logout if 401 response returned from api
        // this.authenticationService.logout(); // todo uncomment this after dev
        location.reload(true);
      }

      const error = err.message || err.statusText;
      return throwError(error);
    }));
  }
}
