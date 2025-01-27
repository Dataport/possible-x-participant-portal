import {Injectable} from '@angular/core';
import {ApiService} from "./api/api.service";

@Injectable({
  providedIn: 'root'
})
export class NameMappingService {
  private idNameMap: { [key: string]: string } = {};
  static readonly UNKNOWN = 'Unknown';
  static readonly EMPTY = '-';

  constructor(private readonly apiService: ApiService) {
  }

  retrieveNameMapping(): Promise<void> {
    console.log("Retrieving name mapping");
    return this.apiService.getNameMapping().then(response => {
      console.log(response);
      this.idNameMap = response;
    }).catch(e => {
      console.log(e);
    });
  }

  getNameById(id: string): string {
    if (!id) {
      return NameMappingService.EMPTY;
    }
    return this.idNameMap[id] || NameMappingService.UNKNOWN;
  }

  getNameMapping(): { [key: string]: string } {
    return this.idNameMap;
  }
}
