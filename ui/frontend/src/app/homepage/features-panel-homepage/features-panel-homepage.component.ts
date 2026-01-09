import { Component, signal } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-features-panel-homepage',
  imports: [PanelModule, CardModule],
  templateUrl: './features-panel-homepage.component.html',
  styleUrl: './features-panel-homepage.component.css',
})
export class FeaturesPanelHomepageComponent {

 dataFeaturesPanel = signal<FeaturesPanelHomepageData[]>([
    {
      icon: "🏨",
      title: "Flight Booking",
      description: "Search and book flights from thousands of airlines worldwide with real-time pricing."
    },
    {
      icon: "🏨",
      title: "Hotel Reservations",
      description: "Find and reserve hotels with dynamic pricing based on demand and availability."
    },
    {
      icon: "🕒",
      title: "Itinerary Management",
      description: "Create and manage your travel itineraries with automated reminders and alerts."
    },
    {
      icon: "💳",
      title: "Secure Payments",
      description: "Process payments securely with multiple payment gateway integrations."
    },
    {
      icon: "🏨",
      title: "Travel Insurance",
      description: "Protect your journey with comprehensive travel insurance options."
    },
    {
      icon: "🏨",
      title: "24/7 Support",
      description: "Get round-the-clock customer support for all your travel needs."
    }
  ]);

}
interface FeaturesPanelHomepageData {
  // headingTitle:String;
  // aboutTitle:String;
  icon: string;
  title: string;
  description: string;
}
