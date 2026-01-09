import { Component, signal } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-dashboard',
  imports: [PanelModule, CardModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {

 dataFeaturesPanel = signal<UserDataDashboard[]>([
  {
    icon: "pi pi-calendar",
    title: "3",
    description: "Total Bookings",
    dynamicColor: "var(--p-blue-600)" // Blue
  },
  {
    icon: "pi pi-check-circle",
    title: "2",
    description: "Confirmed",
    dynamicColor: "var(--p-green-600)" // Green
  },
  {
    icon: "pi pi-clock",
    title: "5",
    description: "Pending",
    dynamicColor: "var(--p-yellow-500)" // Amber
  },
  {
    icon: "pi pi-credit-card",
    title: "$1525",
    description: "Total Spent",
    dynamicColor: "var(--p-purple-500)" // Indigo
  }
]);

}
interface UserDataDashboard {
  // headingTitle:String;
  // aboutTitle:String;
  icon: string;
  title: string;
  description: string;
  dynamicColor: string;
}

