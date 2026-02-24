import { Component, computed, inject, signal } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-rating-and-reviews',
  imports: [CardModule],
  templateUrl: './rating-and-reviews.component.html',
  styleUrl: './rating-and-reviews.component.css',
})
export class RatingAndReviewsComponent {
  private sanitizer = inject(DomSanitizer);

  ratingReviewData = signal<RatingReview[]>([
    {
      icon: '<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M22 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path>',
      headingData: '2M+',
      headingAbout: 'Happy Customers',
    },
    {
      icon: '<circle cx="12" cy="12" r="10"></circle><path d="M12 2a14.5 14.5 0 0 0 0 20 14.5 14.5 0 0 0 0-20"></path><path d="M2 12h20"></path>',
      headingData: '150+',
      headingAbout: 'Countries',
    },
    {
      icon: '<path d="M17.8 19.2 16 11l3.5-3.5C21 6 21.5 4 21 3c-1-.5-3 0-4.5 1.5L13 8 4.8 6.2c-.5-.1-.9.1-1.1.5l-.3.5c-.2.5-.1 1 .3 1.3L9 12l-2 3H4l-1 1 3 2 2 3 1-1v-3l3-2 3.5 5.3c.3.4.8.5 1.3.3l.5-.2c.4-.3.6-.7.5-1.2z"></path>',
      headingData: '50K+',
      headingAbout: 'Daily Flights',
    },
    {
      icon: '<path d="M11.525 2.295a.53.53 0 0 1 .95 0l2.31 4.679a2.123 2.123 0 0 0 1.595 1.16l5.166.756a.53.53 0 0 1 .294.904l-3.736 3.638a2.123 2.123 0 0 0-.611 1.878l.882 5.14a.53.53 0 0 1-.771.56l-4.618-2.428a2.122 2.122 0 0 0-1.973 0L6.396 21.01a.53.53 0 0 1-.77-.56l.881-5.139a2.122 2.122 0 0 0-.611-1.879L2.16 9.795a.53.53 0 0 1 .294-.906l5.165-.755a2.122 2.122 0 0 0 1.597-1.16z"></path>',
      headingData: '4.9',
      headingAbout: 'Rating',
    },
  ]);

  ratingReviewDataSanitized = computed(() => {
    return this.ratingReviewData().map((feature) => ({
      ...feature,
      icon: this.sanitizer.bypassSecurityTrustHtml(feature.icon as string),
    }));
  });
}

interface RatingReview {
  icon: string;
  headingData: string;
  headingAbout: string;
}
