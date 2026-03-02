import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
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
import { hotelapiservice } from '../../core/services/hotel-booking/hotelapiservice';

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
  isSearchResults: boolean = false; // Flag to toggle UI between landing and search results

  constructor(
    private fb: FormBuilder, 
    private hotelService: hotelapiservice,
    private messageService: MessageService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone 
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
   * Loads the top-rated hotels. Sets isSearchResults to false.
   */
  fetchInitial(): void {
    this.isLoading = true;
    this.isSearchResults = false; 
    this.hotelService.getInitialHotels().subscribe({
      next: (data) => this.processAndMapData(data),
      error: (err) => this.handleError(err)
    });
  }

  /**
   * Executes search. Sets isSearchResults to true upon success.
   */
  searchHotels(): void {
    const { destination, checkin, checkout } = this.hotelForm.value;

    if (this.hotelForm.invalid) {
      this.messageService.add({ 
        severity: 'warn', 
        summary: 'Incomplete Search', 
        detail: 'Please fill in destination and both dates.' 
      });
      return;
    }

    const checkInStr = this.formatDate(checkin);
    const checkOutStr = this.formatDate(checkout);

    this.isLoading = true;
    this.availableHotels = []; 

    this.hotelService.searchHotels(destination.trim(), checkInStr, checkOutStr).subscribe({
      next: (data) => {
        this.isSearchResults = true; 
        if (data && data.length > 0) {
          this.processAndMapData(data);
        } else {
          this.isLoading = false;
          this.availableHotels = [];
          this.messageService.add({ severity: 'info', summary: 'No Results', detail: `No hotels available in ${destination}.` });
        }
      },
      error: (err) => this.handleError(err)
    });
  }

  /**
   * Smoothly scrolls user back to the search form when they click 'Check Availability'
   */
  scrollToSearch(): void {
    const element = document.getElementById('search-section');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  private formatDate(date: Date): string {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  private processAndMapData(data: any[]): void {
    this.zone.run(() => {
      this.availableHotels = data.map(hotel => {
        const avgRating = hotel.averageRating || 0;
        return {
          ...hotel,
          displayPrice: hotel.price || hotel.basePrice,
          displayRating: avgRating > 0 ? avgRating.toFixed(1) : 'New',
          displayReviews: hotel.reviewCount || (hotel.ratings ? hotel.ratings.length : 0),
          cachedImage: `https://picsum.photos/seed/${hotel.hotelName}/400/300`,
          starsArray: Array(Math.round(avgRating || 5)).fill(0),
          processedAmenities: (hotel.amenities || []).map((a: string) => ({
            label: a,
            icon: this.getAmenityIcon(a)
          }))
        };
      });
      this.isLoading = false;
      this.cdr.detectChanges();
    });
  }

  private handleError(err: any) {
    this.isLoading = false;
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
}