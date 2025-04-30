// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  mobile: string;
  role: string;
}

export interface RegisterResponse {
  fullName: string;
  email: string;
  mobile: string;
  role: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  mobile: string;
  role: string;
  fullName: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8081';
  private registerUrl = `${this.baseUrl}/registration-service/api/auth/register`;
  private loginUrl = `${this.baseUrl}/login-service/api/auth/login`;

  constructor(private http: HttpClient, private router: Router) { }

  register(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(this.registerUrl, data);
  }

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.loginUrl, data);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('fullName');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserRole(): string | null {
    return localStorage.getItem('userRole');
  }

  isAdmin(): boolean {
    return localStorage.getItem('userRole') === 'ADMIN';
  }

  getCurrentUser(): any {
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('userEmail');
    const role = localStorage.getItem('userRole');
    const fullName = localStorage.getItem('fullName');
    
    if (!token || !email || !role || !fullName) {
      return null;
    }
    
    return {
      token,
      email,
      role,
      fullName
    };
  }
}