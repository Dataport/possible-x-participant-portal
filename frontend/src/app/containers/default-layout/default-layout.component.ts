import {Component, OnInit} from '@angular/core';
import {ApiService} from "../../services/mgmt/api/api.service";
import {AuthService} from "../../services/mgmt/auth/auth.service";

@Component({
  selector: 'app-default-layout',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss']
})
export class DefaultLayoutComponent implements OnInit {
  versionNumber: string = '';
  versionDate: string = '';

  constructor(private readonly apiService: ApiService, protected auth: AuthService) {
  }

  ngOnInit() {
    this.apiService.getVersion().then(response => {
      this.versionNumber = response.version;
      this.versionDate = response.date;
    }).catch(e => {
      console.log(e);
    });
  }
}
