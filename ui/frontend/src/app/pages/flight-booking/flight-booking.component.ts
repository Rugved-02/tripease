import { Component, inject, OnInit, signal, computed, WritableSignal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';

// PrimeNG Imports
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';

// Service & Types
import { FlightBookingService } from '../../core/services/flight-booking/flight-booking.service';
import { ProgressSpinner } from "primeng/progressspinner";
import { AuthService } from '../../core/services/auth/auth-service';
import { BookingRequestDTO } from '../../core/services/booking/dto/BookingRequestDTO';
import { FlightDTO } from '../../core/services/flight-booking/dto/FlightDTO';
import { BookingService } from '../../core/services/booking/booking.service';
import { PaymentService } from '../../core/services/payment/payment.service';

@Component({
  selector: 'app-flight-booking',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    FormsModule,
    ToastModule,
    SelectModule,
    InputTextModule,
    CurrencyPipe,
    ProgressSpinner
],
  templateUrl: './flight-booking.component.html',
  styleUrls: ['./flight-booking.component.css'],
  providers: [MessageService],
})
export class FlightBookingComponent implements OnInit {
  // --- Services ---
  private authService = inject(AuthService);
  private bookingService = inject(BookingService);
  private paymentService = inject(PaymentService);
  private messageService = inject(MessageService);
  private flightService = inject(FlightBookingService);
  private sanitizer = inject(DomSanitizer);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  // --- Signals State Management ---
  private allFlights = signal<FlightDTO[]>([]); // Source of truth from API
  isLoading = signal<boolean>(false);
  selectedFilter = signal<string>('All');

  // --- Computed Signal (Auto-filters whenever allFlights or selectedFilter changes) ---
  filteredFlights = computed(() => {
    const flights = this.allFlights();
    const filter = this.selectedFilter();
    
    if (filter === 'All') return flights;
    return flights.filter(f => f.class?.toLowerCase() === filter.toLowerCase());
  });

  searchForm!: FormGroup;

  passengerOptions = [
    { label: '1 Adult', value: 1 },
    { label: '2 Adults', value: 2 },
    { label: '3 Adults', value: 3 },
    { label: '4+ Adults', value: 4 },
  ];

  flightIconSvg = `<svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17.8 19.2 16 11l3.5-3.5C21 6 21.5 4 21 3c-1-.5-3 0-4.5 1.5L13 8 4.8 6.2c-.5-.1-.9.1-1.1.5l-.3.5c-.2.5-.1 1 .3 1.3L9 12l-2 3H4l-1 1 3 2 2 3 1-1v-3l3-2 3.5 5.3c.3.4.8.5 1.3.3l.5-.2c.4-.3.6-.7.5-1.2z"></path></svg>`;

  ngOnInit() {
    this.searchForm = this.fb.group({
      from: ['', Validators.required],
      to: ['', Validators.required],
      departureDate: [new Date().toISOString().split('T')[0], Validators.required],
      passengers: [this.passengerOptions[0]]
    });

    // Initial load check
    this.onSearch();
  }

  getSafeIcon(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.flightIconSvg);
  }

  /**
   * Triggers the search and updates the 'allFlights' signal
   */
  onSearch() {
    const { from, to, departureDate, passengers } = this.searchForm.value;
    const fromTrimmed = from?.trim();
    const toTrimmed = to?.trim();

    if (!fromTrimmed || !toTrimmed) {
      this.allFlights.set([]);
      return;
    }

    this.isLoading.set(true);
    
    this.flightService.getFlights(fromTrimmed, toTrimmed, departureDate, passengers?.value ?? 1).subscribe({
      next: (data: any[]) => {
        if (!data || data.length === 0) {
          this.allFlights.set([]);
          this.messageService.add({ 
            severity: 'info', 
            summary: 'No Flights', 
            detail: 'Try different cities or dates' 
          });
        } else {
          // Map backend DTO to Frontend Flight interface
          const mapped = data.map(item => ({
            id: item.flightId, 
            airline: item.airline || 'N/A',
            flightNo: item.flightNo || 'N/A',
            class: item.classType || 'Economy',
            depTime: item.depTime || '--:--',
            depCity: item.depPlace || fromTrimmed,
            arrTime: item.arrTime || '--:--',
            arrCity: item.arrPlace || toTrimmed,
            duration: 'Direct', 
            price: item.price || 0,
            seats: item.availableSeats ?? 0
          }));
          this.allFlights.set(mapped);
        }
        this.isLoading.set(false);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.allFlights.set([]);
        this.messageService.add({ 
          severity: err.status === 404 ? 'warn' : 'error', 
          summary: 'Search Failed', 
          detail: 'Unable to fetch flights. Please check your connection.' 
        });
      }
    });
  }

  /**
   * Updates the filter signal (which triggers the computed filteredFlights)
   */
  filterByClass(flightClass: string) {
    this.selectedFilter.set(flightClass);
  }

  /**
   * Booking logic
   */
  onBook(flight: any) {

    if (!this.authService.isLoggedIn()) {
    // Redirect to login if not authenticated
    this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
    return;
  }

  const date = this.searchForm.get('departureDate')?.value;
  const qty = this.searchForm.get('passengers')?.value?.value ?? 1;

  const bookingRequestDTO: BookingRequestDTO = {
      resourceId: flight.id,
      resourceType: "FLIGHT", // e.g., 'FLIGHT'
      subType: flight.class,     // e.g., 'ECONOMY'
      startDate: date,
      endDate: null,
      quantity: qty,
      totalAmount: flight.price
    };

    this.bookingService.createBooking(bookingRequestDTO).subscribe({
    next: (res) => this.paymentService.preparePaymentEntry(res.bookingReference, res.totalAmount),
    error: (err) => console.error(err)
  });
  

    

    // this.flightService.reserveSeats(flight.id, flight.class, date, qty).subscribe({
    //   next: (res) => {
    //     this.messageService.add({ severity: 'success', summary: 'Reserved', detail: res });
    //     this.router.navigate(['/payment'], {
    //       queryParams: { id: flight.flightNo, price: flight.price },
    //     });
    //   },
    //   error: () => {
    //     this.messageService.add({ severity: 'error', summary: 'Booking Failed', detail: 'Seats might not be available' });
    //   }
    // });
  }

  


  trackByFlightNo(index: number, flight: FlightDTO): string {
    return flight.flightNo;
  }
}