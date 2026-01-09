import { Routes } from '@angular/router';
import path from 'node:path';
import { FeaturesPanelHomepageComponent } from './features-panel-homepage/features-panel-homepage.component';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
    { path:"featuresPanelHomepage", component:FeaturesPanelHomepageComponent},
    { path:"dashboard", component:DashboardComponent}
];
