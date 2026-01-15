import { Component, inject, OnInit } from '@angular/core';
import { PrimeIcons, MenuItem } from 'primeng/api';
import { Menubar } from 'primeng/menubar';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { Ripple } from 'primeng/ripple';
import { NgClass, NgIf } from '@angular/common';
import { SelectModule } from 'primeng/select';
import { DesignTokens } from '@primeuix/themes/types';


@Component({
    selector: 'app-menubar',
    imports: [Menubar, SelectModule, CardModule, FormsModule, InputTextModule, ButtonModule, RouterLink, BadgeModule, AvatarModule],
    templateUrl: './menubar.component.html',
    styleUrl: './menubar.component.css',
})
export class MenubarComponent implements OnInit {
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
                routerLink: ['/homepage']
            },
            {
                label: 'Hotels',
                icon: PrimeIcons.WAREHOUSE,
                routerLink: ['']
            },
            {
                label: 'Itinerary',
                icon: PrimeIcons.CALENDAR,
                routerLink: ['']
            },
            {
                label: 'Analytics',
                icon: PrimeIcons.CHART_BAR,
                routerLink: ['/analytics']
            }
        ]
    }
    directLogin() {
        this.router.navigate(['/login']);
    }

    directRegister() {
        this.router.navigate(['/register']);
    }

    toggleDarkMode() {
        const element = document.querySelector('html');
        // console.log(element);
        if (element != null) {
            element.classList.toggle('p-dark');
            this.dark = !this.dark;
        }
    }
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
}