import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthServiceService } from '../services/auth/auth-service.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthServiceService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = this.authService.getToken();
    const role = this.authService.getUserRole();

    if (!token) {
      this.toastr.error('You must be logged in');
      this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return false;
    }

    const allowedRoles = route.data['roles'] as Array<string>;
    if (allowedRoles && !allowedRoles.includes(role!)) {
      this.toastr.warning('Access denied');
      this.router.navigate(['/events']);
      return false;
    }

    return true;
  }
}
