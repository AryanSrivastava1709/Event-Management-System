// src/app/dashboard/admin-dashboard/admin-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  template: `
    <div class="min-h-screen bg-gray-100">
      <nav class="bg-white shadow-sm">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between h-16">
            <div class="flex">
              <div class="flex-shrink-0 flex items-center">
                <h1 class="text-xl font-bold">Event Management System</h1>
              </div>
            </div>
            <div class="flex items-center">
              <div class="ml-3 relative">
                <div class="flex items-center space-x-4">
                  <span class="text-gray-700">Welcome, {{ fullName }} (Admin)</span>
                  <button 
                    (click)="logout()" 
                    class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  >
                    Logout
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </nav>

      <div class="py-10">
        <header>
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h1 class="text-3xl font-bold leading-tight text-gray-900">Admin Dashboard</h1>
          </div>
        </header>
        <main>
          <div class="max-w-7xl mx-auto sm:px-6 lg:px-8">
            <div class="px-4 py-8 sm:px-0">
              <div class="border-4 border-dashed border-gray-200 rounded-lg h-96 p-4">
                <p class="text-gray-600">Admin dashboard content here...</p>
                <p class="mt-4 text-gray-600">Access to event management, user management, and system settings.</p>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  `
})
export class AdminDashboardComponent implements OnInit {
  fullName: string = '';

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName') || 'Admin';
  }

  logout(): void {
    this.authService.logout();
  }
}


