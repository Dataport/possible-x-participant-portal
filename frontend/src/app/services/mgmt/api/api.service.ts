import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable  } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { RestApplicationClient } from './backend';
import { AngularHttpClientImpl } from './angular-http-client';

@Injectable({
  providedIn: 'root'
})
export class ApiService extends RestApplicationClient {

  private baseUrl: string = environment.api_url;

  constructor(private http: HttpClient) {
    super(new AngularHttpClientImpl(http, environment.api_url));
  }

  getHealth(): Observable<any> {
    return this.http.get(`${this.baseUrl}/health`);
  }

}
