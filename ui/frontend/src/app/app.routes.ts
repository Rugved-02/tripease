import { Routes } from '@angular/router';
import { Landing } from './landing/landing';
import { Register } from './register/register';
import { Login } from './login/login';
import { MenubarComponent } from './components/menubar-component/menubar-component';
import { Homepage } from './components/homepage/homepage';

export const routes: Routes = [
    {path:'',component:Landing},
    {path:'register',component:Register},
    {path:'login',component:Login},
    { path:"", component:Homepage},
    { path:"menubar", component:MenubarComponent}
    
    ];
