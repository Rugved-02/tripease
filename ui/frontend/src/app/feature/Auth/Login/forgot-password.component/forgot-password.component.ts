import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputOtpModule } from 'primeng/inputotp';
import { MessageModule } from 'primeng/message';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-forgot-password.component',
  imports: [InputTextModule,
            FloatLabelModule,
            ReactiveFormsModule,
            ButtonModule,
            InputOtpModule,
            MessageModule ,
            CardModule ,
            ToastModule
          ],
  providers:[MessageService],        
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css',
})
export class ForgotPasswordComponent implements OnInit {
        step:number=1;
        emailForm !: FormGroup;
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

        onSubmit(){

          if(this.emailForm.valid){
              this.step=2;
          }
          else {
            // Mark all fields as touched to trigger validation messages
           this.emailForm.markAllAsTouched();
         }
        } 

        submitMsg(){
             this.messageService.add({ 
             severity: 'success', 
             summary: 'Verified', 
             detail: 'Verification Successful!',
             life: 4000 // Duration in milliseconds
           });
        }
}
