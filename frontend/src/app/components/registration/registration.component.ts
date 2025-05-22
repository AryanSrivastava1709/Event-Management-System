import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthServiceService } from '../../services/auth/auth-service.service';
import { ToastrService } from 'ngx-toastr';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-registration',
  imports: [ReactiveFormsModule,RouterLink],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  registerForm!:FormGroup;
  loading:boolean = false;
  roles = ['USER', 'ADMIN'];

  constructor(private fb:FormBuilder,private authService:AuthServiceService,private toastr:ToastrService,private router:Router){}

  ngOnInit():void{
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      mobile: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      role: ['USER', [Validators.required]]
    }, { validators: this.matchPassword });
  }

  matchPassword(group: FormGroup) {
    return group.get('password')?.value === group.get('confirmPassword')?.value
      ? null : { notMatching: true };
  }

  onSubmit(){
    if(this.registerForm.invalid){
      this.toastr.warning("Please fill all the fields correctly","Validation");
      return;
    }

    this.loading = true;
    const{confirmPassword, ...data} = this.registerForm.value;

    this.authService.register(data).subscribe({
      next: () => {
        this.toastr.success('Registered successfully!', 'Success');
        this.router.navigate(['/login']);
        this.loading = false;
      },
      error: (err) => {
        this.toastr.error(err.error.error || 'Registration failed.', 'Error');
        this.loading = false;
      }
    });
  }

}
