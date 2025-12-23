import { Routes } from '@angular/router';
import { Landing } from './components/landing/landing';
import { Register } from './components/register/register';
import { Login } from './components/login/login';
import { MenubarComponent } from './components/menubar-component/menubar-component';
import { Homepage } from './components/homepage/homepage';

export const routes: Routes = [
    // {path:'',component:Landing},
    {path:'register',component:Register},
    {path:'login',component:Login},
    { path:"", redirectTo:"/menubar", pathMatch:'full'},
    { path:"homepage", component:Homepage},
    { path:"menubar", component:MenubarComponent}
    
    ];
