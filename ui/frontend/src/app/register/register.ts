import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputMaskModule } from 'primeng/inputmask';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService ,User} from '../auth-service';
@Component({
  selector: 'app-register',
  imports: [InputTextModule,
            InputNumberModule,
            FloatLabelModule,
            InputMaskModule,
            PasswordModule,
            ButtonModule,
            ReactiveFormsModule,
            FormsModule],          
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
     constructor(private router:Router,private authService:AuthService){}

     login(){
      this.router.navigate(['/login'])
     }
     toggleDark(){
      document.body.classList.toggle('dark');
     }

     form1 = new FormGroup({
         username: new FormControl('', [Validators.required]),
         password: new FormControl('', [Validators.required, Validators.minLength(6)])
      });

     onSubmit(){
        if(this.form1.valid){
          const newuser:User=this.form1.value as User;

          this.authService.addUser(newuser);
          alert("Registration Successful")

        }


}
}
