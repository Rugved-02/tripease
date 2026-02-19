import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { 
  FormBuilder, 
  FormGroup, 
  Validators, 
  ReactiveFormsModule, 
  AbstractControl, 
  ValidationErrors 
} from '@angular/forms';
import { Auth ,User} from '../services/auth'; 
import { Router, RouterLink } from '@angular/router'; // 1. Added RouterLink here

// PrimeNG Modules
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { MessageService } from 'primeng/api';
import { DividerModule } from 'primeng/divider';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    InputTextModule, 
    PasswordModule, 
    ButtonModule, 
    CheckboxModule, 
    ToastModule,
    CardModule,
    DividerModule,
    RouterLink 
  ],
  providers: [MessageService],
  templateUrl: './signup.html',
  styleUrls: ['./signup.css']
})
export class Signup {
  signupForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private authService: Auth, 
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      password: ['', [
        Validators.required, 
        Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$')
      ]],
      confirmPassword: ['', Validators.required],
      terms: [false, Validators.requiredTrue]
    }, { 
      validators: this.passwordMatchValidator 
    });
  }

  get f() { 
    return this.signupForm.controls; 
  }

  // 3. Navigation method (Alternative to using routerLink in HTML)
  login() {
    this.router.navigate(['/login']);
  }
  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirm = control.get('confirmPassword')?.value;
    return password !== confirm ? { passwordMismatch: true } : null;
  }

  submit() {
    if (this.signupForm.invalid) {
      this.signupForm.markAllAsTouched();
      this.messageService.add({
        severity: 'warn',
        summary: 'Form Incomplete', 
        detail: 'Please fill in all required fields correctly.',
        life: 3000
      });
    } 
    else {
      const formData:User = this.signupForm.value as User ;
      this.authService.addUser(formData);
     

      this.messageService.add({
        severity: 'success', 
        summary: 'Account Created', 
        detail: 'Welcome to TripEase! Redirecting to login...',
        life: 2000
      });

      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    }
  }
}