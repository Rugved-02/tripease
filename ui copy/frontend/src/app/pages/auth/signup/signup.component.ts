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
import { AuthService, UserCredentials } from '../../../core/services/auth/auth-service';
import { Router, RouterLink } from '@angular/router';

import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { MessageService } from 'primeng/api';
import { DividerModule } from 'primeng/divider';
import { LogoTextComponent } from "../../../shared/components/logo-text/logo-text.component";

@Component({
  selector: 'app-signup',
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
    LogoTextComponent
],
  providers: [MessageService],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {

  signupForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private authService: AuthService, 
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
      const formData:UserCredentials = this.signupForm.value as UserCredentials ;
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
