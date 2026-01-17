import { Component, signal } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { CommonModule, NgIf } from '@angular/common';
import { MenuItem } from 'primeng/api';




@Component({
  selector: 'app-dashboard',
  imports: [ButtonModule,CommonModule,PanelModule, CardModule, MenuModule, NgIf],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {



 dataFeaturesPanel = signal<UserDataDashboard[]>([
  {
    icon: "pi pi-calendar",
    title: "5",
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
    title: "3",
    description: "Pending",
    dynamicColor: "var(--p-yellow-500)" // Amber
  },
  {
    icon: "pi pi-credit-card",
    title: "$1,525",
    description: "Total Spent",
    dynamicColor: "var(--p-purple-500)" // Indigo
  }
]);

quickActions = signal([
 {
   icon: 'pi pi-send',
   label: 'Book Flight'
 },
 {
   icon: 'pi pi-building',
   label: 'Reserve Hotel'
 },
 {
   icon: 'pi pi-calendar',
   label: 'View Itinerary'
 }
]);

recentBookings = signal([
 {
   icon: 'pi pi-send',
   title: 'Delta Airlines - DL 1234 | JFK → LAX',
   date: 'Feb 15, 2026',
   price: '$350',
   status: 'Confirmed'
 },
 {
   icon: 'pi pi-building',
   title: 'Grand Plaza Hotel - Los Angeles | 3 nights',
   date: 'Feb 15, 2026',
   price: '$750',
   status: 'Confirmed'
 },
 {
   icon: 'pi pi-send',
   title: 'United Airlines - UA 5678 | LAX → JFK',
   date: 'Feb 20, 2026',
   price: '$425',
   status: 'Pending'
 }
]);


upcomingTrip = {

  title: 'Upcoming Trip Reminder',

  message: 'Your Los Angeles Business Trip is coming up in 15 days.                                  Make sure all arrangements are confirmed!'

};


}
interface UserDataDashboard {
  // headingTitle:String;
  // aboutTitle:String;
  icon: string;
  title: string;
  description: string;
  dynamicColor: string;
}
