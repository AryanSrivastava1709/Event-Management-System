// registration.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './registration.component.html'
})
export class RegistrationComponent {
  registerForm: FormGroup;
  isSubmitting = false;
  errorMessage = '';
  roles = ['USER', 'ADMIN'];

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      mobile: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      role: ['USER', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.markFormGroupTouched(this.registerForm);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';

    this.http.post('http://localhost:8081/registration-service/api/auth/register', this.registerForm.value)
      .subscribe({
        next: (response) => {
          console.log('Registration successful', response);
          this.isSubmitting = false;
          // Navigate to login page or show success message
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Registration failed', error);
          this.isSubmitting = false;
          if (error.error && error.error.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'Registration failed. Please try again.';
          }
        }
      });
  }

  markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if ((control as any).controls) {
        this.markFormGroupTouched(control as FormGroup);
      }
    });
  }

  // Helper methods for form validation
  get fullName() { return this.registerForm.get('fullName'); }
  get email() { return this.registerForm.get('email'); }
  get password() { return this.registerForm.get('password'); }
  get mobile() { return this.registerForm.get('mobile'); }
  get role() { return this.registerForm.get('role'); }
}