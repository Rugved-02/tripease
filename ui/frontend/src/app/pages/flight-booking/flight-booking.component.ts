import { Component, inject, OnInit, signal, computed } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

// PrimeNG Imports
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { ProgressSpinner } from 'primeng/progressspinner';

// Service & Types
import { FlightBookingService } from '../../core/services/flight-booking/flight-booking.service';
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
    ProgressSpinner,
  ],
  templateUrl: './flight-booking.component.html',
  styleUrls: ['./flight-booking.component.css'],
  providers: [MessageService],
})
export class FlightBookingComponent implements OnInit {
  private authService = inject(AuthService);
  private bookingService = inject(BookingService);
  private paymentService = inject(PaymentService);
  private messageService = inject(MessageService);
  private flightService = inject(FlightBookingService);
  private sanitizer = inject(DomSanitizer);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  // --- Signals State Management ---
  private allFlights = signal<FlightDTO[]>([]);
  isLoading = signal<boolean>(false);
  selectedFilter = signal<string>('All');
  isInitialLoad = signal<boolean>(true); // NEW: Track if showing top 10 vs search results

  // --- Computed Signal ---
  filteredFlights = computed(() => {
    const flights = this.allFlights();
    const filter = this.selectedFilter();
    if (filter === 'All') return flights;
    return flights.filter((f) => f.class?.toLowerCase() === filter.toLowerCase());
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
      passengers: [this.passengerOptions[0]],
    });

    this.route.queryParams.subscribe(params => {
      if (params['from'] || params['to'] || params['date']) {
        this.isInitialLoad.set(false);
        this.searchForm.patchValue({
          from: params['from'] || '',
          to: params['to'] || '',
          departureDate: params['date'] || new Date().toISOString().split('T')[0]
        });

        if (params['from'] && params['to']) {
          this.onSearch();
        }
      } else {
        // Fetch top 10 flights if no search params exist
        this.fetchFeaturedFlights();
      }
    });
  }

  fetchFeaturedFlights() {
    this.isLoading.set(true);
    this.isInitialLoad.set(true);
    // Calling the new CSR architecture endpoint
    this.flightService.getInitialFlights().subscribe({
      next: (data) => {
        this.mapAndSetFlights(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load flights' });
      }
    });
  }

  private mapAndSetFlights(data: any[]) {
    const mapped = data.map((item) => ({
      id: item.flightId,
      airline: item.airline || 'N/A',
      flightNo: item.flightNo || 'N/A',
      class: item.classType || 'Economy',
      depTime: item.depTime || '--:--',
      depCity: item.depPlace || 'N/A',
      arrTime: item.arrTime || '--:--',
      arrCity: item.arrPlace || 'N/A',
      duration: 'Direct',
      price: item.price || 0,
      seats: item.availableSeats ?? 0,
    }));
    this.allFlights.set(mapped);
  }

  onSearch() {
    const { from, to, departureDate, passengers } = this.searchForm.value;
    if (!from?.trim() || !to?.trim()) return;

    this.isLoading.set(true);
    this.isInitialLoad.set(false);

    this.flightService
      .getFlights(from.trim(), to.trim(), departureDate, passengers?.value ?? 1)
      .subscribe({
        next: (data: any[]) => {
          this.mapAndSetFlights(data);
          this.isLoading.set(false);
          if (data.length === 0) {
            this.messageService.add({ severity: 'info', summary: 'No Flights', detail: 'Try different criteria' });
          }
        },
        error: () => {
          this.isLoading.set(false);
          this.allFlights.set([]);
        },
      });
  }

  // --- UX: PRE-POPULATE AND SLIDE ---
  onCheckAvailability(flight: any) {
    // 1. Pre-populate the form with the specific flight's route
    this.searchForm.patchValue({
      from: flight.depCity,
      to: flight.arrCity
    });

    // 2. Smoothly slide/scroll up to the search box
    // We target the top of the page where the form is located
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  getSafeIcon(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.flightIconSvg);
  }

  filterByClass(flightClass: string) {
    this.selectedFilter.set(flightClass);
  }

  onBook(flight: any) {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }
    const bookingRequestDTO: BookingRequestDTO = {
      resourceId: flight.id,
      resourceType: 'FLIGHT',
      subType: flight.class,
      startDate: this.searchForm.get('departureDate')?.value,
      endDate: null,
      quantity: this.searchForm.get('passengers')?.value?.value ?? 1,
      totalAmount: flight.price,
    };

    this.bookingService.createBooking(bookingRequestDTO).subscribe({
      next: (res) => {
        this.messageService.add({ severity: 'info', summary: 'Success', detail: 'Flight added to booking' });
        setTimeout(() => this.paymentService.preparePaymentEntry(res.bookingReference, res.totalAmount), 2000);
      },
      error: (err) => console.error(err),
    });
  }

  trackByFlightNo(index: number, flight: FlightDTO): string {
    return flight.flightNo;
  }
}