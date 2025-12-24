import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
import { MenubarComponent } from '../menubar.component/menubar.component';


@Component({
  selector: 'app-login',
  imports: [InputTextModule,ButtonModule,FloatLabelModule,PasswordModule, MenubarComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class Login {
      constructor(private router:Router){}
      register(){
      this.router.navigate(['/register'])
     }
     toggleDark(){
      document.body.classList.toggle('dark');
     }
}
