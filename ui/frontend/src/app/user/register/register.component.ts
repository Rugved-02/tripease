import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputMaskModule } from 'primeng/inputmask';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { MenubarComponent } from '../../shared/components/menubar/menubar.component';
@Component({
  selector: 'app-register',
  imports: [MenubarComponent,InputTextModule,InputNumberModule,FloatLabelModule,InputMaskModule,PasswordModule,ButtonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class Register {
     constructor(private router:Router){}

     login(){
      this.router.navigate(['/login'])
     }
    //  toggleDark(){
    //   document.body.classList.toggle('dark');
    //  }
}
