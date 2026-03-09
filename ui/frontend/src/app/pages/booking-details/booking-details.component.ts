import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CurrencyPipe, DatePipe, NgIf, NgFor, TitleCasePipe, CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BookingService } from '../../core/services/booking/booking.service';
import { RecentBookingsResponseDTO } from '../../core/services/dashboard/dto/RecentBookingsResponseDTO';

@Component({
  selector: 'app-booking-details',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule, CurrencyPipe, DatePipe, NgIf, NgFor, RouterLink, TitleCasePipe],
  templateUrl: './booking-details.component.html',
  styleUrl: './booking-details.component.css',
})
export class BookingDetailsComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private bookingService = inject(BookingService);

  // Use the DTO type for the signal
  bookingData = signal<RecentBookingsResponseDTO | null>(null);
  isLoading = signal<boolean>(false);
  bookingId =signal<string>("");

  ngOnInit() {
    // 1. Listen for ID changes
    this.route.params.subscribe(params => {
      const id = params['bookingId'];
      if (id) {
        this.loadBookingDetails(id);
      }
    });
  }

  loadBookingDetails(id: string) {
    this.bookingId.set(id);
    this.isLoading.set(true);
    
    this.bookingService.getBookingDetails(id).subscribe({
      next: (response: RecentBookingsResponseDTO) => {
        // Mapping the response directly to our signal
        this.bookingData.set(response);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error fetching booking:', err);
        this.isLoading.set(false);
      }
    });
  }

  goToDashboard() {
  this.router.navigate(['/dashboard']); // Ensure Router is injected in constructor
}
}