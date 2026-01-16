import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,        // 👈 needed for <a routerLink>
    RouterLinkActive,  // 👈 optional, for active link styling
    ToastModule
  ],
  template: `
    <p-toast></p-toast>
    
    <router-outlet></router-outlet>
  `
})
export class App {}
