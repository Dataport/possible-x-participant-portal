import {Component, OnInit} from '@angular/core';
import {ApiService} from "../../services/mgmt/api/api.service";

@Component({
  selector: 'app-default-layout',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss']
})
export class DefaultLayoutComponent implements OnInit {
  versionNumber: string = '';
  versionDate: string = '';
  authToken: string | null = null;

  constructor(private apiService: ApiService) {
  }

  ngOnInit() {
    this.authToken = sessionStorage.getItem('authToken');
    this.apiService.getVersion().then(response => {
      this.versionNumber = response.version;
      this.versionDate = response.date;
    });
  }
}
