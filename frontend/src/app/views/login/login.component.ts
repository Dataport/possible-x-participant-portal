import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private router: Router)  {}

  login(username: string, password: string) {
    var authToken = btoa(username + ':' + password);
    sessionStorage.setItem('authToken', authToken);
    this.username = '';
    this.password = '';
    this.router.navigate(['/offer/provide']).then(() => {
      window.location.reload();
    });
  }
}
