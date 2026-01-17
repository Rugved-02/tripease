import { Routes } from '@angular/router';
// import { Register } from './user/register/register.component';
// import { Login } from './user/login/login.component';
import { MenubarComponent } from './shared/components/menubar/menubar.component';
import { Homepage } from './homepage/homepage/homepage.component';
import { AboutUs } from './shared/components/about-us/about-us';
import { SearchFlightHotelsHomepageComponent } from './homepage/search-flight-hotels-homepage/search-flight-hotels-homepage.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { FeaturesPanelHomepageComponent } from './homepage/features-panel-homepage/features-panel-homepage.component';
import { PaymentComponent } from './payment/payment.component';
import { CardPaymentMethodComponent } from './payment/card-payment-method.component/card-payment-method.component';
import { MainLayoutComponent } from './main-layout/main-layout.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { SignupComponent } from './pages/auth/signup/signup.component';
import { FlightBookingComponent } from './pages/flight-booking/flight-booking.component';
import { HotelBookingComponent } from './pages/hotel-booking/hotel-booking.component';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { ItineraryPlanningComponent } from './pages/itinerary-planning/itinerary-planning.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'homepage', pathMatch: 'full' },
  { path: 'signup', component: SignupComponent },
  { path: 'login', component: LoginComponent },
  
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: 'homepage',
        component: Homepage,
      },
      {
        path: 'bookFlight',
        component: FlightBookingComponent,
      },
      {
        path: 'bookHotel',
        component: HotelBookingComponent,
      },
      {
        path: 'analytics',
        component: AnalyticsComponent,
        canActivate:[authGuard]
      },
      {
        path: 'itineraryPlanning',
        component: ItineraryPlanningComponent,
        canActivate:[authGuard]
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate:[authGuard]
      },
      {
        path: 'payment',
        component: PaymentComponent,
        canActivate:[authGuard]
      },
    ],
  },
];
