import { Component,OnInit } from '@angular/core';
import { FloatLabelModule } from 'primeng/floatlabel';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { AuthService } from '../../../core/services/auth/auth-service';
import { MessageService } from 'primeng/api';
import { CheckboxModule } from 'primeng/checkbox';
import { RouterLink } from '@angular/router';
import { LogoTextComponent } from "../../../shared/components/logo-text/logo-text.component";

@Component({
  selector: 'app-login',
  imports: [InputTextModule,
    ButtonModule,
    FloatLabelModule,
    PasswordModule,
    ReactiveFormsModule,
    CardModule,
    MessageModule,
    ToastModule,
    CheckboxModule,
    RouterLink, LogoTextComponent],
          providers:[MessageService],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
      loginForm!: FormGroup;
      isLoading: boolean = false;

      constructor(
        private router:Router,
        private fb:FormBuilder,
        private messageService: MessageService,
        private authService : AuthService
      ){}


      ngOnInit(): void {
        // Initialize the form with validation rules
        this.loginForm = this.fb.group({
          email: ['', [Validators.required,Validators.email]],
          password: ['', [Validators.required, Validators.minLength(6)]]
        });
      }

     isInvalid(controlName: string): boolean {
      const control = this.loginForm.get(controlName);
      return !!(control && control.invalid && (control.dirty || control.touched));
      }



    onSubmit(): void {
      if (this.loginForm.valid) {
        this.isLoading = true;
        const { email, password } = this.loginForm.value;

        // Subscribe to the Observable returned by the service
        this.authService.checkAuth(email, password).subscribe({
          next: (isAuthenticated) => {
            this.isLoading = false;

            if (isAuthenticated) {
              this.messageService.add({
                severity: 'success',
                summary: 'Success',
                detail: 'Login Successful!',
                life: 2000
              });

              setTimeout(() => {
                this.router.navigate(['/dashboard']);
              }, 2000);
            } else {
              this.showError('Invalid email or password');
            }
          },
          error: (err) => {
            this.isLoading = false;
            this.showError('An unexpected error occurred. Please try again later.');
            console.error('Login error:', err);
          }
        });
      } else {
        this.loginForm.markAllAsTouched();
      }
    }


    private showError(message: string): void {
      this.messageService.add({
        severity: 'error',
        summary: 'Login Failed',
        detail: message,
        sticky: true
      });
    }

    signup(){
      this.router.navigate(['/signup'])
     }
    toggleDark(){
      const element = document.querySelector('html');
      element?.classList.toggle('dark');
     }
    onForgotPassword(){
       this.router.navigate(['/forgotPassword'])
     }
}