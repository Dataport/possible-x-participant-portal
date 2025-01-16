import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable, catchError, throwError, of} from 'rxjs';
import {Router} from "@angular/router";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router)  {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    var authToken = sessionStorage.getItem('authToken');
    if (authToken) {
      req = req.clone({
        setHeaders: {
          Authorization: `Basic ${authToken}`
        }
      });
    }
    return next.handle(req).pipe(catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        sessionStorage.removeItem('authToken');
        console.log(error);
        alert(`Invalid Credentials used. Please log in again.`);
      } else if (error.status === 403) {
        sessionStorage.removeItem('authToken');
        console.log(error);
        alert(`Unauthorized Credentials used. Please log in with the correct Role.`);
      } else {
        sessionStorage.removeItem('authToken');
        console.log(error)
        alert('Something went wrong.');
      }
      this.router.navigate(["/login"]).then(() => {
        window.location.reload();
      });
      return throwError(() => new Error(error.message));
    }));
  }
}
