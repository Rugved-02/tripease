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
import { LoginComponent } from './pages/auth/login/login/login.component';

export const routes: Routes = [
  { path:'', redirectTo:"homepage", pathMatch:'full'},
  // { path:'register',component:Register},
  { path:'login',component:LoginComponent},
//   { path: 'homepage', component: Homepage },
  { path: 'menubar', component: MenubarComponent },
  { path: 'aboutUs', component: AboutUs },
  { path: 'searchFlightHomepage', component: SearchFlightHotelsHomepageComponent },
  { path: 'footer', component: FooterComponent },
  { path: 'featuresPanelHomepage', component: FeaturesPanelHomepageComponent },
  { path: 'payment', component: PaymentComponent },
  { path: 'cardPaymentMethod', component: CardPaymentMethodComponent },
  { path: '', component: MainLayoutComponent, children:[{
    path: 'homepage', component:Homepage    
  }] }
];
