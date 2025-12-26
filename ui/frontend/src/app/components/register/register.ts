import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputMaskModule } from 'primeng/inputmask';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { MenubarComponent } from '../menubar-component/menubar-component';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    MenubarComponent,
    InputTextModule,
    InputNumberModule,
    FloatLabelModule,
    InputMaskModule,
    PasswordModule,
    ButtonModule,
    CardModule,
    CommonModule,
    ToastModule,
    PasswordModule,
    ReactiveFormsModule
  ],
  providers: [MessageService],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  registerForm: FormGroup;
  isDarkMode: boolean = false;
  constructor(private fb: FormBuilder, private router: Router, private messageService: MessageService) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      phone: ['', [Validators.required, Validators.pattern('^[6-9][0-9]{9}$')]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        // Pattern: 1 Uppercase, 1 Lowercase, 1 Number, 1 Special, Min 8 Chars
        Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&].{7,}')
      ]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  // 2. MUST DECLARE THIS METHOD
  toggleDark() {
    this.isDarkMode = !this.isDarkMode;
    if (this.isDarkMode) {
      document.body.classList.add('dark'); 
    } else {
      document.body.classList.remove('dark');
    }
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('password')?.value === g.get('confirmPassword')?.value
      ? null : { 'mismatch': true };
  }
    register() {
      if (this.registerForm.valid) {
        localStorage.setItem('user_auth', JSON.stringify(this.registerForm.value));
        this.messageService.add({ 
          severity: 'success', 
          summary: 'Registration Successful', 
          detail: 'Redirecting to Login...', 
          life: 2000 
    });

      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 2000);
    } else {
      this.messageService.add({ 
        severity: 'error', 
        summary: 'Form Invalid', 
        detail: 'Please check all fields.' 
      });
    }
  }

  login() {
    this.router.navigate(['/login']);
  }

}

