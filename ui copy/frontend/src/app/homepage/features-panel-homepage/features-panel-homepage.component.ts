import { Component, computed, inject, signal } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-features-panel-homepage',
  imports: [PanelModule, CardModule],
  templateUrl: './features-panel-homepage.component.html',
  styleUrl: './features-panel-homepage.component.css',
})
export class FeaturesPanelHomepageComponent {
  private sanitizer = inject(DomSanitizer);

  dataFeaturesPanel = signal<FeaturesPanelHomepageData[]>([
    {
      icon: '<path d="M17.8 19.2 16 11l3.5-3.5C21 6 21.5 4 21 3c-1-.5-3 0-4.5 1.5L13 8 4.8 6.2c-.5-.1-.9.1-1.1.5l-.3.5c-.2.5-.1 1 .3 1.3L9 12l-2 3H4l-1 1 3 2 2 3 1-1v-3l3-2 3.5 5.3c.3.4.8.5 1.3.3l.5-.2c.4-.3.6-.7.5-1.2z"></path>',
      title: 'Flight Booking',
      description:
        'Search and book flights from thousands of airlines worldwide with real-time pricing.',
    },
    {
      icon: '<path d="M10 22v-6.57"></path><path d="M12 11h.01"></path><path d="M12 7h.01"></path><path d="M14 15.43V22"></path><path d="M15 16a5 5 0 0 0-6 0"></path><path d="M16 11h.01"></path><path d="M16 7h.01"></path><path d="M8 11h.01"></path><path d="M8 7h.01"></path><rect x="4" y="2" width="16" height="20" rx="2"></rect>',
      title: 'Hotel Reservations',
      description: 'Find and reserve hotels with dynamic pricing based on demand and availability.',
    },
    {
      icon: '<path d="M8 2v4"></path><path d="M16 2v4"></path><rect width="18" height="18" x="3" y="4" rx="2"></rect><path d="M3 10h18"></path>',
      title: 'Itinerary Management',
      description: 'Create and manage your travel itineraries with automated reminders and alerts.',
    },
    {
      icon: '<rect width="20" height="14" x="2" y="5" rx="2"></rect><line x1="2" x2="22" y1="10" y2="10"></line>',
      title: 'Secure Payments',
      description: 'Process payments securely with multiple payment gateway integrations.',
    },
    {
      icon: '<path d="M20 13c0 5-3.5 7.5-7.66 8.95a1 1 0 0 1-.67-.01C7.5 20.5 4 18 4 13V6a1 1 0 0 1 1-1c2 0 4.5-1.2 6.24-2.72a1.17 1.17 0 0 1 1.52 0C14.51 3.81 17 5 19 5a1 1 0 0 1 1 1z"></path>',
      title: 'Travel Insurance',
      description: 'Protect your journey with comprehensive travel insurance options.',
    },
    {
      icon: '<circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline>',
      title: '24/7 Support',
      description: 'Get round-the-clock customer support for all your travel needs.',
    },
  ]);

  sanitizedFeatures = computed(() => {
    return this.dataFeaturesPanel().map((feature) => ({
      ...feature,
      icon: this.sanitizer.bypassSecurityTrustHtml(feature.icon as string),
    }));
  });
}
interface FeaturesPanelHomepageData {
  // headingTitle:String;
  // aboutTitle:String;
  icon: string;
  title: string;
  description: string;
}
