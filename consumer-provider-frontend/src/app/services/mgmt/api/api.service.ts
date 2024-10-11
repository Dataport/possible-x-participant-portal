import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, lastValueFrom  } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ConsumeOfferRequest } from '../model/data';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.api_url;

  constructor(private http: HttpClient) {}

  getHealth(): Observable<any> {
    return this.http.get(`${this.baseUrl}/health`);
  }

  public async createOffer(): Promise<any> {
    return await lastValueFrom(this.http.post(`${this.baseUrl}/provider/offer`, null));
  }

  public async acceptContractOffer(): Promise<any> {
    let request: ConsumeOfferRequest = {
      counterPartyAddress: environment.counter_party_address
    }

    return await lastValueFrom(this.http.post(`${this.baseUrl}/consumer/acceptContractOffer`, request));
  }

}
