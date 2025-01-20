import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable, catchError, throwError} from 'rxjs';
import {Router} from "@angular/router";
import {AuthService} from "../services/mgmt/auth/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router, private auth: AuthService)  {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    var authToken = this.auth.getToken();
    if (authToken) {
      req = req.clone({
        setHeaders: {
          Authorization: `Basic ${authToken}`
        }
      });
    }
    return next.handle(req).pipe(catchError((error: HttpErrorResponse) => {
      if (error.status.valueOf() < 500 && error.status.valueOf() >= 400) {
        this.auth.logout();
        console.log(error);
        switch (error.status) {
          case 401:
            alert(`Invalid Credentials used. Please log in again.`);
            break;
          case 403:
            alert(`Unauthorized Credentials used. Please log in with the correct Role.`);
            break
        }
        this.router.navigate(["/login"]).then(() => {
          window.location.reload();
        });
      } else {
        console.log(error)
        alert('Something went wrong.');
      }
      return throwError(() => new Error(error.message));
    }));
  }
}
