import {Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {ApiService} from "./api/api.service";

@Injectable({
  providedIn: 'root'
})
export class NameMappingService {
  private idNameMap: { [key: string]: string } = {};
  private isNameMappingLoaded = false;

  constructor(private apiService: ApiService) {
  }

  private async retrieveNameMapping(): Promise<void> {
    console.log("Retrieving name mapping");
    return this.apiService.getNameMapping().then(response => {
      console.log(response);
      this.idNameMap = response;
      this.isNameMappingLoaded = true;
    }).catch((e: HttpErrorResponse) => {
      console.log(e?.error?.detail || e?.error || e?.message);
    });
  }

  private ensureNameMappingLoaded(): Promise<void> {
    if (!this.isNameMappingLoaded) {
      return this.retrieveNameMapping();
    }
    return Promise.resolve();
  }

  async getNameById(id: string): Promise<string> {
    if (!id) {
      return '-';
    }
    await this.ensureNameMappingLoaded();
    return this.idNameMap[id] || "Unknown";
  }

  async getNameMapping(): Promise<{ [key: string]: string }> {
    await this.ensureNameMappingLoaded();
    return this.idNameMap;
  }
}
