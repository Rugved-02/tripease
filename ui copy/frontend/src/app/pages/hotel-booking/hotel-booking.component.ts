import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';

// PrimeNG Imports
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ImageModule } from 'primeng/image';
import { ToastModule } from 'primeng/toast';
import { DatePickerModule } from 'primeng/datepicker'; 
import { MessageService } from 'primeng/api'; 
import { hotelapiservice } from '../../core/services/hotel-booking/hotelapiservice';
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { RatingModule } from 'primeng/rating';

@Component({
  selector: 'app-hotel-booking',
  standalone: true,
  templateUrl: './hotel-booking.component.html',
  styleUrl: './hotel-booking.component.css',
  providers: [MessageService],
  imports: [
    CommonModule, ReactiveFormsModule, CardModule, ButtonModule, 
    InputTextModule, ImageModule, ToastModule, DatePickerModule,
    ProgressSpinnerModule, RatingModule, TagModule
  ]
})
export class HotelBookingComponent implements OnInit {
  hotelForm!: FormGroup;
  availableHotels: any[] = [];
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder, 
    private hotelService: hotelapiservice,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone // Added for performance
  ) {}

  ngOnInit(): void {
    this.hotelForm = this.fb.group({ 
      destination: [''],
      checkin: [null],
      checkout: [null]
    });
    this.fetchHotels();
  }

  // Pre-calculate icons during data mapping instead of in HTML loop
  getAmenityIcon(amenity: string): string {
    const key = amenity.trim().toLowerCase();
    const iconMap: { [key: string]: string } = {
      'wifi': 'pi-wifi',
      'pool': 'pi-th-large',
      'spa': 'pi pi-map-marker',
      'gym': 'pi-bolt',
      'restaurant': 'pi-shop',
      'bar': 'pi-receipt',
      'sea view': 'pi-image'
    };
    return iconMap[key] || 'pi-check-circle';
  }

  fetchHotels(location?: string): void {
  this.isLoading = true;
  
  this.hotelService.getHotels(location).subscribe({
    next: (data) => {
      this.zone.run(() => {
        this.availableHotels = data.map(hotel => {
          
          // Use the actual averageRating from backend, or default to 0
          const actualRating = hotel.averageRating || 0;
          const actualReviews = hotel.reviewCount || 0;

          return {
            ...hotel,
            displayRating: actualRating > 0 ? actualRating.toFixed(1) : 'No Ratings',
            displayReviews: actualReviews,
            cachedImage: `https://picsum.photos/seed/${hotel.hotelId}/400/300`,
            
            // Generate stars based on ACTUAL rating value
            // We use Math.round to decide how many full stars to show
            starsArray: Array(Math.round(actualRating)).fill(0),
            
            processedAmenities: (hotel.amenities || []).map((a: string) => ({
              label: a,
              icon: this.getAmenityIcon(a)
            }))
          };
        });
        this.isLoading = false;
        this.cdr.markForCheck();
      });
    },
    error: (err) => {
      this.isLoading = false;
      this.messageService.add({ 
        severity: 'error', 
        summary: 'Error',
        detail: 'Failed to fetch hotel data' 
      });
    }
  });
}

  searchHotels(): void {
    const location = this.hotelForm.get('destination')?.value?.trim() || '';
    this.fetchHotels(location);
  }

  // Prevents re-rendering the whole list when searching
  trackByHotelId(index: number, hotel: any): string {
    return hotel.hotelId;
  }
}