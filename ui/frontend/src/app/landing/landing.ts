import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import {FloatLabelModule} from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
@Component({
  selector: 'app-landing',
  imports: [InputTextModule, FloatLabelModule,ButtonModule,PasswordModule],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})

export class Landing {
     constructor(private router:Router){}

     login(){
      this.router.navigate(['/login'])
     }
     register(){
      this.router.navigate(['/register'])
     }
}
