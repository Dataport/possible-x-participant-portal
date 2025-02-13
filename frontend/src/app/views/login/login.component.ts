import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/mgmt/auth/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private readonly router: Router, private readonly auth: AuthService) {
  }

  login(username: string, password: string) {
    this.auth.login(username, password);
    this.username = '';
    this.password = '';
    this.router.navigate(['/offer/provide']).then(() => {
      window.location.reload();
    });
  }
}
