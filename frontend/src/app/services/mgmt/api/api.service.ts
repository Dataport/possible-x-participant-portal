import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environments/environment';
import {RestApplicationClient} from './backend';
import {AngularHttpClientImpl} from './angular-http-client';

@Injectable({
  providedIn: 'root'
})
export class ApiService extends RestApplicationClient {

  private readonly baseUrl: string = environment.api_url;

  constructor(private readonly http: HttpClient) {
    super(new AngularHttpClientImpl(http, environment.api_url));
  }

}
