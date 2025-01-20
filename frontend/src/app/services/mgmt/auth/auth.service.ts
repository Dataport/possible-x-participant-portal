import { Injectable } from '@angular/core';

const TOKEN_NAME : string = 'authToken';

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private isAuthenticated = !!sessionStorage.getItem(TOKEN_NAME);

  login(username: string, password: string): void {
    var authToken = btoa(username + ':' + password);
    sessionStorage.setItem(TOKEN_NAME, authToken);
    this.isAuthenticated = true;
  }

  logout(): void {
    sessionStorage.removeItem(TOKEN_NAME);
    this.isAuthenticated = false;
  }

  isLoggedIn(): boolean {
    return this.isAuthenticated;
  }

  getToken(): string | null {
    return sessionStorage.getItem(TOKEN_NAME);
  }
}
