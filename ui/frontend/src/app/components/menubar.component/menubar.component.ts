import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Menubar } from 'primeng/menubar';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { ActivatedRoute, Router, RouterLink } from "@angular/router";



@Component({
    selector: 'app-menubar-component',
    imports: [Menubar, CardModule, FormsModule, InputTextModule, FloatLabel, ButtonModule, RouterLink],
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
                label: 'Home',
                icon: 'pi pi-home',
                routerLink: ['/homepage']
            },
            {
                label: 'Features',
                icon: 'pi pi-star',
                routerLink: ['']
            },
            {
                label: 'Projects',
                icon: 'pi pi-search',
                items: [
                    {
                        label: 'Components',
                        icon: 'pi pi-bolt'
                    },
                    {
                        label: 'Blocks',
                        icon: 'pi pi-server'
                    },
                    {
                        label: 'UI Kit',
                        icon: 'pi pi-pencil'
                    },
                    {
                        label: 'Templates',
                        icon: 'pi pi-palette',
                        items: [
                            {
                                label: 'Apollo',
                                icon: 'pi pi-palette'
                            },
                            {
                                label: 'Ultima',
                                icon: 'pi pi-palette'
                            }
                        ]
                    }
                ]
            },
            {
                label: 'Contact',
                icon: 'pi pi-envelope'
            }
        ]
    }
    directLogin() {
        this.router.navigate(['/login']);
    }

    directRegister() {
        this.router.navigate(['/register']);
    }

    toggleDark() {
        document.body.classList.toggle('dark');
        this.dark = true;

    }
    toggleLight() {
        document.body.classList.toggle('dark');
        this.dark = false;
    }

}
