import {Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {ApiService} from "./api/api.service";

@Injectable({
  providedIn: 'root'
})
export class NameMappingService {
  private idNameMap: { [key: string]: string } = {};
  private sortedIdNameMap: { [key: string]: string } = {};

  constructor(private apiService: ApiService) {
  }

  async retrieveNameMapping() {
    console.log("Retrieving name mapping");
    this.apiService.getNameMapping().then(response => {
      console.log(response);
      this.idNameMap = response;
      this.sortedIdNameMap = this.getAlphabeticallySortedNameMapping(this.idNameMap);
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

  getSortedNameMapping(): { [key: string]: string } {
    return this.sortedIdNameMap;
  }

  private getAlphabeticallySortedNameMapping(map: { [key: string]: string }): { [key: string]: string } {
    const sortedMap: { [key: string]: string } = {};
    Object.entries(map)
      .sort(([, a], [, b]) => a.localeCompare(b))
      .forEach(([key, value]) => {
        sortedMap[key] = value;
      });
    return sortedMap;
  }
}
