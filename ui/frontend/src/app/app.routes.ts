import { Routes } from '@angular/router';
import { Landing } from './landing/landing';
import { Register } from './register/register';
import { Login } from './login/login';
import { ForgotPasswordComponent } from './forgot-password.component/forgot-password.component';

export const routes: Routes = [
    {path:'',component:Landing},
    {path:'register',component:Register},
    {path:'login',component:Login},
    {path:'forgotPassword',component:ForgotPasswordComponent}
    
    ];
