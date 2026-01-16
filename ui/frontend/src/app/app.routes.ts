import { Routes } from '@angular/router';
import { Signup } from './signup/signup';
import { Hotels } from './hotels/hotels';
import { Login } from './login/login';
 
export const routes: Routes = [
  { path: 'signup', component: Signup },
  { path: 'hotels', component: Hotels  },
  { path: 'login', component: Login},
  { path: '', redirectTo: '/login', pathMatch: 'full' }


];
