import {HttpClient as AngularHttpClient, HttpParams} from '@angular/common/http';
import {lastValueFrom, map, Observable} from 'rxjs';
import {HttpClient, RestResponse} from './backend';


export class AngularHttpClientImpl implements HttpClient {

  constructor(private readonly http: AngularHttpClient, private readonly baseUrl: string | undefined = undefined) {
    console.log(baseUrl);
  }

  request<R>(requestConfig: {
    method: string;
    url: string;
    queryParams?: any;
    data?: any;
    copyFn?: (data: R) => R;
  }): RestResponse<R> {
    const {method, url, queryParams, data, copyFn} = requestConfig;

    let fullUrl = url;
    if (this.baseUrl) {
      fullUrl = new URL(url, this.baseUrl).href
    }

    let params = new HttpParams();

    if (queryParams) {
      Object.keys(queryParams).forEach(key => {
        params = params.set(key, queryParams[key]);
      });
    }

    let request: Observable<R>;

    switch (method.toLowerCase()) {
      case 'get':
        request = this.http.get<R>(fullUrl, {params});
        break;
      case 'patch':
        request = this.http.patch<R>(fullUrl, data, {params});
        break;
      case 'post':
        request = this.http.post<R>(fullUrl, data, {params});
        break;
      case 'put':
        request = this.http.put<R>(fullUrl, data, {params});
        break;
      case 'delete':
        request = this.http.delete<R>(fullUrl, {params});
        break;
      default:
        throw new Error(`Unsupported request method: ${method}`);
    }

    return lastValueFrom(request.pipe(
      map((response: R) => copyFn ? copyFn(response) : response)
    ));
  }
}
