import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [InputTextModule,ButtonModule,FloatLabelModule,PasswordModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
      constructor(private router:Router){}
      register(){
      this.router.navigate(['/register'])
     }
}
