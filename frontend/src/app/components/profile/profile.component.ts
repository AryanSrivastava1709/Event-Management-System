import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthServiceService } from '../../services/auth/auth-service.service';

interface UserProfile {
  email: string;
  fullName: string;
  mobile: string;
  role: string;
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userProfile: UserProfile = {
    email: '',
    fullName: '',
    mobile: '',
    role: ''
  };

  constructor(private router: Router,private authService:AuthServiceService) { }

  ngOnInit(): void {
    this.loadUserProfile();
  }

  /**
   * Load user profile from localStorage
   */
  private loadUserProfile(): void {
    try {
      const email = localStorage.getItem('email') || '';
      const fullName = localStorage.getItem('fullName') || '';
      const mobile = localStorage.getItem('mobile') || '';
      const role = localStorage.getItem('role') || 'USER';

      this.userProfile = {
        email: email,
        fullName: fullName,
        mobile: mobile,
        role: role
      };
    } catch (error) {
      console.error('Error loading user profile:', error);
    }
  }

  /**
   * Get display name for user role
   */
  getRoleDisplayName(): string {
    switch (this.userProfile.role) {
      case 'ADMIN':
        return 'Event Organizer';
      case 'USER':
        return 'Event Attendee';
      default:
        return 'Member';
    }
  }

  logout(): void {
    this.authService.logout();
  }
}