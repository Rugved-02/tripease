import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Landing } from './components/landing/landing';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';



@Component({
  selector: 'app-root',
  imports: [RouterOutlet,ButtonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  dark:boolean=false;
  constructor(private router:Router){}
    
    //  toggleDark(){
    //   document.body.classList.toggle('dark');
    //   this.dark=true;
     
    //  }
    //  toggleLight(){
    //   document.body.classList.toggle('dark');
    //   this.dark=false;
    //  }

     home(){
      this.router.navigate(['/'])
     }
     
}
