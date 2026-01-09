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
    DividerModule
  ],
  providers: [MessageService],
  templateUrl: './signup.html',
  styleUrls: ['./signup.css']
})
export class Signup {
  signupForm: FormGroup;

  constructor(private fb: FormBuilder, private messageService: MessageService) {
    this.signupForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      // Pattern for a standard 10-digit format
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      password: ['', [
        Validators.required, 
        // Requires 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char
        Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$')
      ]],
      confirmPassword: ['', Validators.required],
      terms: [false, Validators.requiredTrue]
    }, { 
      // This validator checks if password and confirmPassword match
      validators: this.passwordMatchValidator 
    });
  }

  
  get f() { 
    return this.signupForm.controls; 
  }

  
  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirm = control.get('confirmPassword')?.value;
    
    if (password !== confirm) {
      
      return { passwordMismatch: true };
    }
    return null;
  }

  
  submit() {
    if (this.signupForm.invalid) {
      // Highlights all errors if the user tries to submit too early
      this.signupForm.markAllAsTouched();
      
      this.messageService.add({
        severity: 'warn', // Using 'warn' for a distinct color (usually orange)
        summary: 'Form Incomplete', 
        detail: 'Please fill in all required fields correctly.',
        life: 3000
      });
      return;
    }

    
    console.log('Form Submitted successfully:', this.signupForm.value);
    
    this.messageService.add({
      severity: 'success', 
      summary: 'Account Created', 
      detail: 'Welcome to TripEase! Redirecting...',
      life: 3000
    });

    
  }
}