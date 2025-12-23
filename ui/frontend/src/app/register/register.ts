import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputMaskModule } from 'primeng/inputmask';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-register',
  imports: [InputTextModule,InputNumberModule,FloatLabelModule,InputMaskModule,PasswordModule,ButtonModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
     constructor(private router:Router){}

     login(){
      this.router.navigate(['/login'])
     }
     toggleDark(){
      document.body.classList.toggle('dark');
     }
}
