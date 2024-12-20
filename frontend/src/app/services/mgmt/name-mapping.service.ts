import {Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {ApiService} from "./api/api.service";

@Injectable({
  providedIn: 'root'
})
export class NameMappingService {
  private idNameMap: { [key: string]: string } = {};

  constructor(private apiService: ApiService) {
  }

  retrieveNameMapping(): Promise<void> {
    console.log("Retrieving name mapping");
    return this.apiService.getNameMapping().then(response => {
      console.log(response);
      this.idNameMap = response;
    }).catch((e: HttpErrorResponse) => {
      console.log(e?.error?.detail || e?.error || e?.message);
    });
  };

  getNameById(id: string): string {
    return this.idNameMap[id];
  }

  getNameMapping(): { [key: string]: string } {
    return this.idNameMap;
  }
}
