import { Component, ElementRef, HostListener, inject, OnInit, signal } from '@angular/core';
import { PrimeIcons, MenuItem } from 'primeng/api';
import { Menubar } from 'primeng/menubar';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { Ripple } from 'primeng/ripple';
import { NgClass, NgIf } from '@angular/common';
import { SelectModule } from 'primeng/select';
import { DesignTokens } from '@primeuix/themes/types';
import { AuthService } from '../../../core/services/auth/auth-service';
import { LogoTextComponent } from "../logo-text/logo-text.component";
import { ButtonModule } from 'primeng/button';
import { DashboardService } from '../../../core/services/dashboard/dashboard.service';
import { UserResponseDTO } from '../../../core/services/dashboard/dto/UserResponseDTO';

@Component({
  selector: 'app-menubar-component',
  imports: [
    Menubar,
    SelectModule,
    CardModule,
    FormsModule,
    InputTextModule,
    FloatLabel,
    ButtonModule,
    RouterLink,
    BadgeModule,
    AvatarModule,
    Ripple,
    NgClass,
    NgIf,
    LogoTextComponent
],
  templateUrl: './menubar.component.html',
  styleUrl: './menubar.component.css',
})
export class MenubarComponent implements OnInit {

  private authService = inject(AuthService);
  private dashboardService = inject(DashboardService);
  private eRef = inject(ElementRef);

  showProfileMenu:boolean = false;
  
  items: MenuItem[] | undefined;

  value1: string | undefined;

  value2: string | undefined;

  value3: string | undefined;

  router = inject(Router);
  dark: boolean = false;

  ngOnInit() {
    this.items = [
      {
        label: 'Flights',
        icon: 'custom-plane',
        routerLink: ['/bookFlight'],
      },
      {
        label: 'Hotels',
        icon: PrimeIcons.WAREHOUSE,
        routerLink: ['/bookHotel'],
      },
      {
        label: 'Itinerary',
        icon: PrimeIcons.CALENDAR,
        routerLink: ['/itineraryPlanning'],
      },
      {
        label: 'Analytics',
        icon: PrimeIcons.CHART_BAR,
        routerLink: ['/analytics'],
      },
    ];

    this.loadUserProfileDetails();
  }

  userProfileDetails = signal<UserResponseDTO | null>(null);

  loadUserProfileDetails(): void {
    this.dashboardService.getLoggedInUserDetails().subscribe({
      next: (response)=> {
        this.userProfileDetails.set(response);
        console.log(response);
      },
      error: (err) => console.log(err)
    })
  }

  directLogin() {
    this.router.navigate(['/login']);
  }

  directSignup() {
    this.router.navigate(['/signup']);
  }

  toggleProfileMenu() {
    this.showProfileMenu = !this.showProfileMenu;
  }

  @HostListener('document:click', ['$event'])
  clickout(event: Event) {
    // Check if the click was outside the component's element
    if (!this.eRef.nativeElement.contains(event.target)) {
      this.showProfileMenu = false;
    }
  }

  isLoggedIn(){
    return this.authService.isLoggedIn();
  }

  onLogout(){
    localStorage.removeItem('token'); 
    localStorage.removeItem('currentUser');
    // If you use sessionStorage, use: sessionStorage.clear();

    // 2. (Optional) Clear any user state in a service if you have one
    // this.authService.currentUser.set(null);

    // 3. Redirect to login page
    
    this.router.navigate(['/login']);
    
    console.log('User logged out successfully');
  }

  redirectToDashboard(){
    this.showProfileMenu = false;
    this.router.navigate(['/dashboard']);
  }

  redirectToTrips(){
    this.showProfileMenu = false;
    this.router.navigate(['/itineraryPlanning']);
  }

  redirectToAnalytics(){
    this.showProfileMenu = false;
    this.router.navigate(['/analytics']);
  }


  loginButtonStyle = {
    '--p-button-primary-background': 'var(--p-primary-0)',
    '--p-button-border-radius': '10px',
    '--p-button-sm-font-size': '0.7rem',
    '--p-button-primary-color': 'var(--p-primary-1000)',
    '--p-button-primary-hover-background': 'var(--p-primary-1050)',
    '--p-button-primary-hover-color': 'var(--p-primary-1000)',
    '--p-button-primary-border-color': 'var(--p-primary-0)',
    '--p-button-primary-hover-border-color': 'var(--p-primary-0)',
    '--p-button-primary-active-border-color': 'var(--p-primary-0)',
  };

  signUpButtonStyle = {
    '--p-button-primary-background': 'var(--p-primary-1000)',
    '--p-button-border-radius': '10px',
    '--p-button-sm-font-size': '0.7rem',
    '--p-button-primary-hover-background': 'var(--p-primary-1000)',
    '--p-button-primary-border-color': 'var(--p-primary-1000)',
    '--p-button-primary-hover-border-color': 'var(--p-primary-1000)',
    '--p-button-primary-active-border-color': 'var(--p-primary-1000)',
  };

  // toggleDarkMode() {
  //     const element = document.querySelector('html');
  //     if (element != null) {
  //         element.classList.toggle('p-dark');
  //         this.dark = !this.dark;
  //     }
  // }

  // customDesignTokenButton = {
  //     menubar: {
  //         // The main container tokens are nested under 'root'
  //         root: {
  //             background: '{surface.0}',
  //             borderColor: '{blue.200}',
  //             borderRadius: '200px',
  //             padding: '0.60rem 1.5rem',
  //             gap: '0.5rem'
  //         }
  //     },
  //     item: {
  //             focusBackground: '{primary.200}',
  //             // activeBackground: '{blue.800}',
  //             focusColor: '{primary.700}',
  //             color: '{surface.700}',
  //             borderRadius: '8px',
  //             padding: '0.5rem 0.75rem',
  //             gap: '0.5rem'
  //         }
  // };

  // customDesignTokenButtonMenubar = {
  //     button: {
  //         root: {
  //             borderRadius: '100px',
  //             primary: {
  //                 background: '{blue.400}',
  //                 borderColor: '{blue.400}',
  //                 color: '{primary.400}'

  //             }
  //         }
  //     }
  // };

  // loginButtonDT = {
  //     borderRadius: '10px',
  //     sm:{
  //         fontSize:'0.7rem'
  //     },
  //     primary: {
  //         color: 'var(--p-surface-1000)',
  //         hoverColor:'var(--p-surface-1000)',
  //         background: 'var(--p-surface-0)',
  //         hoverBackground: '#0000001a',
  //         borderColor:'var(--p-surface-0)',
  //         hoverBorderColor:'var(--p-surface-0)',
  //         activeBorderColor:'var(--p-surface-0)'

  //     }
  // }
}
