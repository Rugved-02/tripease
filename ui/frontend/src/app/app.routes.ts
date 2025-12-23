import { Routes } from '@angular/router';
import { MenubarComponent } from './components/menubar-component/menubar-component';
import { Homepage } from './components/homepage/homepage';

export const routes: Routes = [
    { path:"", component:Homepage},
    { path:"menubar", component:MenubarComponent}
];
