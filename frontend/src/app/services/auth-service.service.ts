import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterRequest, RegisterResponse } from '../model/register';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse } from '../model/login';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  private baseUrl = 'http://localhost:8081';
  private registerUrl = `${this.baseUrl}/registration-service/api/auth/register`;
  private loginUrl = `${this.baseUrl}/login-service/api/auth/login`;


  constructor(private http:HttpClient,private router:Router) { }

  register(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(this.registerUrl, data);
  }

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.loginUrl, data);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    localStorage.removeItem('fullName');
    localStorage.removeItem('mobile')
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserRole(): string | null {
    return localStorage.getItem('role');
  }

  isAdmin(): boolean {
    return localStorage.getItem('role') === 'ADMIN';
  }

  getCurrentUser(): LoginResponse | null {
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('email');
    const role = localStorage.getItem('role');
    const fullName = localStorage.getItem('fullName');
    const mobile = localStorage.getItem('mobile');
    
    if (!token || !email || !role || !fullName || !mobile) {
      return null;
    }
    
    return {
      token,
      email,
      role,
      fullName,
      mobile
    };
  }
}
