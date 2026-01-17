// import { Routes } from '@angular/router';
// import { Landing } from './feature/Homepage/landing/landing';
// import { RegisterComponent } from './feature/Auth/register.component/register.component';
// import { Login } from './feature/Auth/Login/login/login';
// import { ForgotPasswordComponent } from './feature/Auth/Login/forgot-password.component/forgot-password.component';
// import { DashboardComponent } from './feature/Dashboard/dashboard.component/dashboard.component';


// export const routes: Routes = [
//     {path:'',component:Landing},
//     {path:'register',component:RegisterComponent},
//     {path:'login',component:Login},
//     {path:'forgotPassword',component:ForgotPasswordComponent},
//     {path:'dashboard',component:DashboardComponent},
    
// ];


import { Routes } from '@angular/router';
import { RegisterComponent } from './feature/Auth/register.component/register.component';
import { Login } from './feature/Auth/Login/login/login';
import { ForgotPasswordComponent } from './feature/Auth/Login/forgot-password.component/forgot-password.component';
import { DashboardComponent } from './feature/Dashboard/dashboard.component/dashboard.component';
import { Homepage } from './feature/Homepage/home.component/home.component';
import { MenubarComponent } from './shared/components/menubar.component/menubar.component';
import { AboutUs } from './shared/components/about-us/about-us';
import { SearchFlightHotelsHomepageComponent } from './feature/Homepage/search-flight-hotels-homepage.component/search-flight-hotels-homepage.component';
import { Footer } from './shared/components/footer/footer';
import { FeaturesPanelHomepageComponent } from './feature/Homepage/features-panel-homepage.component/features-panel-homepage.component';
import { AnalyticsComponent } from './feature/Analytics/analytics.component/analytics.component';



export const routes: Routes = [
    {path:'',component:Homepage},
    {path:'register',component:RegisterComponent},
    {path:'login',component:Login},
    {path:'forgotPassword',component:ForgotPasswordComponent},
    {path:'dashboard',component:DashboardComponent},
    { path:"homepage", component:Homepage},
    { path:"menubar", component:MenubarComponent},
    { path:"aboutUs", component:AboutUs},
    { path:"searchFlightHomepage", component:SearchFlightHotelsHomepageComponent},
    { path:"footer", component:Footer},
    { path:"featuresPanelHomepage", component:FeaturesPanelHomepageComponent},
    { path:"analytics",component:AnalyticsComponent}
    
];
