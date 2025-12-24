import { Routes } from '@angular/router';
import { Register } from './components/register.component/register.component';
import { Login } from './components/login.component/login.component';
import { MenubarComponent } from './components/menubar.component/menubar.component';
import { Homepage } from './components/homepage.component/homepage.component';

export const routes: Routes = [
    // {path:'',component:Landing},
    {path:'register',component:Register},
    {path:'login',component:Login},
    { path:"", redirectTo:"/menubar", pathMatch:'full'},
    { path:"homepage", component:Homepage},
    { path:"menubar", component:MenubarComponent}
    
    ];
