// src/app/login/login.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService, LoginRequest } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isSubmitting = false;
  errorMessage = '';
  returnUrl: string = '';
  
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }
  
  ngOnInit(): void {
    // Get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    
    // Auto redirect if already logged in
    if (this.authService.isLoggedIn()) {
      this.redirectBasedOnRole();
    }
  }
  
  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }
    
    this.isSubmitting = true;
    this.errorMessage = '';
    
    const loginRequest: LoginRequest = {
      email: this.loginForm.get('email')?.value,
      password: this.loginForm.get('password')?.value
    };
    
    this.authService.login(loginRequest)
      .subscribe({
        next: (response) => {
          // Store the token and user info in localStorage
          localStorage.setItem('token', response.token);
          localStorage.setItem('userRole', response.role);
          localStorage.setItem('userEmail', response.email);
          localStorage.setItem('fullName', response.fullName);
          
          this.redirectBasedOnRole();
        },
        error: (error) => {
          this.isSubmitting = false;
          if (error.status === 401) {
            this.errorMessage = 'Invalid email or password';
          } else {
            this.errorMessage = 'An error occurred. Please try again later.';
          }
          console.error('Login error:', error);
        }
      });
  }
  
  redirectBasedOnRole(): void {
    const role = this.authService.getUserRole();
    if (role === 'ADMIN') {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.router.navigate(['/user/dashboard']);
    }
  }
}