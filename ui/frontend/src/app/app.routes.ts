import { Routes } from '@angular/router';
import { Register } from './user/register/register.component';
import { Login } from './user/login/login.component';
import { MenubarComponent } from './shared/components/menubar/menubar.component';
import { Homepage } from './homepage/homepage/homepage.component';
import { AboutUs } from './shared/components/about-us/about-us';
import { SearchFlightHotelsHomepageComponent } from './homepage/search-flight-hotels-homepage/search-flight-hotels-homepage.component';
import { FooterComponent } from './shared/components/footer/footer.component';

export const routes: Routes = [
    { path:'', redirectTo:"homepage", pathMatch:'full'},
    { path:'register',component:Register},
    { path:'login',component:Login},
    { path:"homepage", component:Homepage},
    { path:"menubar", component:MenubarComponent},
    { path:"aboutUs", component:AboutUs},
    { path:"searchFlightHomepage", component:SearchFlightHotelsHomepageComponent},
    { path:"footer", component:FooterComponent}
    
    ];
