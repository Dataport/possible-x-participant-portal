import {ActivatedRouteSnapshot, ResolveFn, Router, RouterStateSnapshot} from '@angular/router';
import {ApiService} from "../api/api.service";
import {inject} from "@angular/core";
import {AuthService} from "./auth.service";

export const isAuthenticated: ResolveFn<any> =
  async (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> => {
    const router = inject(Router);
    const apiService = inject(ApiService);
    const authService = inject(AuthService);

    try {
      const response = await apiService.getNameMapping();
      return true;
    } catch (e) {
      console.log(e);
      if (e.status === 401 || e.status === 403) {
        authService.logout();
        switch (e.status) {
          case 401:
            alert(`Invalid Credentials used. Please log in again.`);
            break;
          case 403:
            alert(`Unauthorized Credentials used. Please log in with the correct Role.`);
            break
        }
        router.navigate(["/login"]).then(() => {
          window.location.reload();
        });
      }
      return false;
    }
  }
