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
import { Auth } from '../services/auth'; 
import { MessageService } from 'primeng/api';
import { CheckboxModule } from 'primeng/checkbox';
import { RouterLink } from '@angular/router';

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
            CheckboxModule ,
            RouterLink
          ],
  providers:[MessageService],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
      loginForm!: FormGroup;
      constructor(private router:Router,
        private fb:FormBuilder,
        private messageService: MessageService,
        private authService: Auth){}


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
      console.log('Form Submitted!', this.loginForm.value);
      // Process your login or data here

      const { email, password } = this.loginForm.value;

      const isAuthenticated = this.authService.checkAuth(email, password);
      if(isAuthenticated){
          
          this.messageService.add({ 
          severity: 'success', 
          summary: 'Success', 
          detail: 'Login Successful!',
          life: 4000 // Duration in milliseconds
           });
          
          setTimeout(()=>{
            this.router.navigate(['/dashboard']);
          },2000);
      }
      else{
        this.messageService.add({ 
          severity: 'error', 
          summary: 'Failed', 
          detail: 'Login Unsuccessful!',
          //life: 3000// milliseconds
          sticky:true

           });
      }
      

    } 
    else {
      // Mark all fields as touched to trigger validation messages
      this.loginForm.markAllAsTouched();
       }
    }

    register(){
      //this.router.navigate(['/register'])
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

