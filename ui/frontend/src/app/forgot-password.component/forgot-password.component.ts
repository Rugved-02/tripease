import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputOtpModule } from 'primeng/inputotp';
import { MessageModule } from 'primeng/message';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RouterModule } from '@angular/router';
 
@Component({
  selector: 'app-forgot-password.component',
  imports: [InputTextModule,
            FloatLabelModule,
            ReactiveFormsModule,
            FormsModule,
            ButtonModule,
            InputOtpModule,
            MessageModule ,
            CardModule ,
            ToastModule,
            RouterModule
          ],
  providers:[MessageService],        
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css',
})
export class ForgotPasswordComponent implements OnInit {
        step:number=1;
        emailForm !: FormGroup;
        otpValue: any;
        constructor(private fb:FormBuilder,private messageService:MessageService){}
 
       ngOnInit() :void{
        this.emailForm = this.fb.group({
         email: ['', [Validators.required,Validators.email]],
       });
      }
 
        isInvalid(controlName: string,form:FormGroup): boolean {
           const control = this.emailForm.get(controlName);
           return !!(control && control.invalid && (control.dirty || control.touched));
         }
 
        isOtpValid(): boolean {
          if (!this.otpValue) return false;
          const otpString = Array.isArray(this.otpValue) ? this.otpValue.join('') : this.otpValue.toString();
          return otpString.length === 6;
        }
 
        onSubmit(){
 
          if(this.emailForm.valid){
              this.step=2;
          }
          else {
            // Mark all fields as touched to trigger validation messages
           this.emailForm.markAllAsTouched();
         }
        }
 
  submitMsg() {
    // Convert OTP to string if it's an array
    const otp = Array.isArray(this.otpValue) ? this.otpValue.join('') : (this.otpValue || '');
   
    // Check if OTP is valid (6 digits)
    if (!otp || otp.length !== 6) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid OTP',
        detail: 'Please enter all 6 digits of the code.',
        life: 4000
      });
      return;
    }
   
    // Show success message only if OTP is valid
    this.messageService.add({
      severity: 'success',
      summary: 'Login Successful',
      detail: 'Code verified Successfully!',
      life: 4000
    });
   
    // Clear OTP after success
    this.otpValue = '';
  }
 
  resendCode() {
    // Clear the OTP input
    this.otpValue = '';
   
    // Show info message about resending code
    this.messageService.add({
      severity: 'info',
      summary: 'Code Resent',
      detail: 'A new verification code has been sent to your email.',
      life: 4000
    });
  }
}