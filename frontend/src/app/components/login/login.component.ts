import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthServiceService } from '../../services/auth-service.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule,RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm!: FormGroup;
  loading: boolean = false;
  returnUrl: string = '/events';

  constructor(
    private fb: FormBuilder,
    private authService: AuthServiceService,
    private toastr: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  redirectBasedOnRole(): void {
    const role = this.authService.getUserRole();
    if (role === 'ADMIN') {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.router.navigate(['/user/dashboard']);
    }
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/events';

    if (this.authService.isLoggedIn()) {
      if (this.returnUrl) {
        this.router.navigate([this.returnUrl]);
      } else {
        this.redirectBasedOnRole();
      }
    }

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      this.toastr.warning('Please fill all the fields correctly', 'Validation');
      return;
    }

    this.loading = true;
    const data = this.loginForm.value;

    this.authService.login(data).subscribe({
      next: (data) => {
        this.toastr.success('Logged in successfully!', 'Success');
        localStorage.setItem('token', data.token);
        localStorage.setItem('role', data.role);
        localStorage.setItem('email', data.email);
        localStorage.setItem('fullName', data.fullName);
        localStorage.setItem('mobile', data.mobile);

        if (this.returnUrl) {
          this.router.navigate([this.returnUrl]);
        } else {
          this.redirectBasedOnRole();
        }
      },
      error: (err) => {
        this.toastr.error(err.error.error || 'Login failed.', 'Error');
        this.loading = false;
      }
    });
  }
}
