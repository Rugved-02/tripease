import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

// PrimeNG Imports
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ImageModule } from 'primeng/image';
import { ToastModule } from 'primeng/toast';
import { DatePickerModule } from 'primeng/datepicker'; 
import { MessageService } from 'primeng/api'; 
import { TagModule } from 'primeng/tag';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { RatingModule } from 'primeng/rating';

// Service
import { HotelBookingService } from '../../core/services/hotel-booking/hotel-booking.service';
import { AuthService } from '../../core/services/auth/auth-service';
import { BookingService } from '../../core/services/booking/booking.service';
import { PaymentService } from '../../core/services/payment/payment.service';
import { Router } from '@angular/router';
import { BookingRequestDTO } from '../../core/services/booking/dto/BookingRequestDTO';

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

  // --- Signals State Management ---
  availableHotels: WritableSignal<any[]> = signal([]);
  isLoading: WritableSignal<boolean> = signal(false);
  isSearchResults: WritableSignal<boolean> = signal(false);

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);
  private bookingService = inject(BookingService);
  private paymentService = inject(PaymentService);
  private hotelBookingService = inject(HotelBookingService);
  private messageService = inject(MessageService);


  constructor(
  ) {}

  ngOnInit(): void {
    this.hotelForm = this.fb.group({ 
      destination: ['', Validators.required],
      checkin: [null, Validators.required],
      checkout: [null, Validators.required]
    });
    this.fetchInitial(); 
  }

  /**
   * Loads the top-rated hotels. Sets isSearchResults signal to false.
   */
  fetchInitial(): void {
    this.isLoading.set(true);
    this.isSearchResults.set(false); 

    this.hotelBookingService.getInitialHotels().subscribe({
      next: (data: any[]) => this.processAndMapData(data),
      error: (err: any) => console.log(err)
    });
  }

  /**
   * Executes search. Sets isSearchResults signal to true upon success.
   */
  searchHotels(): void {
    if (this.hotelForm.invalid) {
      this.messageService.add({ 
        severity: 'warn', 
        summary: 'Incomplete Search', 
        detail: 'Please fill in destination and both dates.' 
      });
      return;
    }

    const { destination, checkin, checkout } = this.hotelForm.value;
    const checkInStr = this.formatDate(checkin);
    const checkOutStr = this.formatDate(checkout);

    this.isLoading.set(true);
    this.availableHotels.set([]); 

    this.hotelBookingService.searchHotels(destination.trim(), checkInStr, checkOutStr).subscribe({
      next: (data: any) => {
        this.isSearchResults.set(true); 
        if (data && data.length > 0) {
          this.processAndMapData(data);
        } else {
          this.isLoading.set(false);
          this.availableHotels.set([]);
          this.messageService.add({ 
            severity: 'info', 
            summary: 'No Results', 
            detail: `No hotels available in ${destination}.` 
          });
        }
      },
      error: (err: any) => this.handleError(err)
    });
  }

  /**
   * Smoothly scrolls user back to the search form
   */
  scrollToSearch(): void {
    const element = document.getElementById('search-section');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  private formatDate(date: Date): string {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  private processAndMapData(data: any[]): void {
    const mapped = data.map(hotel => {
      const avgRating = hotel.averageRating || 0;

      const index = Math.floor(Math.random() * 8);
      const stableNum = index + 1
      const localImagePath = `assets/hotels/hotel-${stableNum}.jpg`;

      console.log("Hotel Name"+hotel.hotelName);
      return {
        ...hotel,
        displayPrice: hotel.price || hotel.basePrice,
        displayRating: avgRating > 0 ? avgRating.toFixed(1) : 'New',
        displayReviews: hotel.reviewCount || (hotel.ratings ? hotel.ratings.length : 0),
        cachedImage: localImagePath,
        starsArray: Array(Math.round(avgRating || 5)).fill(0),
        processedAmenities: (hotel.amenities || []).map((a: string) => ({
          label: a,
          icon: this.getAmenityIcon(a)
        }))
      };
    });

    this.availableHotels.set(mapped);
    this.isLoading.set(false);
  }

  private handleError(err: any) {
    this.isLoading.set(false);
    this.messageService.add({ 
      severity: 'error', 
      summary: 'Connection Error', 
      detail: 'Server is currently unreachable.' 
    });
  }

  getAmenityIcon(amenity: string): string {
    const key = amenity.toLowerCase();
    if (key.includes('wifi')) return 'pi-wifi';
    if (key.includes('pool')) return 'pi-th-large';
    if (key.includes('spa')) return 'pi-heart';
    if (key.includes('gym')) return 'pi-bolt';
    return 'pi-check-circle';
  }

  trackByHotelName(index: number, hotel: any): string {
    return hotel.hotelName;
  }

    /**
     * Booking logic
     */
    onBook(hotel: any) {
  
      if (!this.authService.isLoggedIn()) {
      // Redirect to login if not authenticated
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    const { checkin, checkout } = this.hotelForm.value;
  
    const bookingRequestDTO: BookingRequestDTO = {
        resourceId: hotel.hotelId,
        resourceType: "HOTEL", // e.g., 'FLIGHT'
        subType: null,     // e.g., representation for flights only 'ECONOMY'
        startDate: this.formatDate(checkin),
        endDate: this.formatDate(checkout),
        quantity: 1,
        totalAmount: hotel.price
      };
  
      this.bookingService.createBooking(bookingRequestDTO).subscribe({
      next: (res) => this.paymentService.preparePaymentEntry(res.bookingReference, res.totalAmount),
      error: (err) => console.error(err)
    });
}
}