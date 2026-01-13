import { Routes } from '@angular/router';
import { Signup } from './signup/signup';
import { Hotels } from './hotels/hotels';
 
export const routes: Routes = [
  { path: '', component: Signup },
  { path: 'hotels', component: Hotels  }


];
