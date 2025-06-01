import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthServiceService } from '../../../services/auth/auth-service.service';

@Component({
  selector: 'app-header',
  imports: [CommonModule,RouterLink,RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  isMenuOpen = false;

  constructor(public authService: AuthServiceService, private router: Router) {}

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  getRole():string | null{
    return this.authService.getUserRole();
  }

  goToHome() : string{
    const role = this.authService.getUserRole();
    if (role === 'ADMIN') {
      return '/admin/dashboard';
    } else if (role === 'USER') {
      return '/user/dashboard';
    }else{
      return '/';
    }
  }
}
