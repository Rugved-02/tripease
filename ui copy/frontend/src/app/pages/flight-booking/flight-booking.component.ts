import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser'; // Add this import
import {
  FlightBookingService,
  Flight,
} from '../../core/services/flight-booking/flight-booking.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-flight-booking',
  imports: [ReactiveFormsModule, CommonModule, FormsModule, ToastModule, FormsModule, SelectModule, InputTextModule, CurrencyPipe],
  templateUrl: './flight-booking.component.html',
  styleUrls: ['./flight-booking.component.css'],
  providers: [MessageService], // PrimeNG Toast Service
})
export class FlightBookingComponent implements OnInit {

  
  private messageService = inject(MessageService);
  private flightService = inject(FlightBookingService);
  
  private sanitizer = inject(DomSanitizer);
  private router = inject(Router);
  private fb = inject(FormBuilder);


  filteredFlights: Flight[] = [];
  selectedFilter: string = 'All';


  searchForm!: FormGroup;

  passengerOptions = [
    { label: '1 Adult', value: '1 Adult' },
    { label: '2 Adults', value: '2 Adults' },
    { label: '3 Adults', value: '3 Adults' },
    { label: '4+ Adults', value: '4+ Adults' },
  ];



  constructor() { }

  flightIconSvg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <path d="M17.8 19.2 16 11l3.5-3.5C21 6 21.5 4 21 3c-1-.5-3 0-4.5 1.5L13 8 4.8 6.2c-.5-.1-.9.1-1.1.5l-.3.5c-.2.5-.1 1 .3 1.3L9 12l-2 3H4l-1 1 3 2 2 3 1-1v-3l3-2 3.5 5.3c.3.4.8.5 1.3.3l.5-.2c.4-.3.6-.7.5-1.2z"></path>
    </svg>`;



  ngOnInit() {
    this.filteredFlights = this.flightService.getFlights("", "");
    this.searchForm = this.fb.group({
      from: ['', Validators.required],
      to: ['', Validators.required],
      departureDate: ['', Validators.required],
      passengers: [this.passengerOptions[0]] // Default to first option
    });
  }
  getSafeIcon(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.flightIconSvg);
  }
  onSearch() {
    this.filteredFlights = this.flightService.getFlights(this.searchForm.get("from")?.value, this.searchForm.get("to")?.value);
    // Feedback for the user
    if (this.filteredFlights.length === 0) {
      this.messageService.add({
        severity: 'warn',
        summary: 'No Flights Found',
        life: 3000,
      });
    }
  }

  // Filter Logic
  filterByClass(flightClass: string) {
    this.filteredFlights = this.flightService.getFlights("", "");
    this.selectedFilter = flightClass;
    if (flightClass !== 'All') {
      this.filteredFlights = this.filteredFlights.filter((f) => f.class === flightClass);
    }
  }

  // Toast notification for "Book Now"
  onBook(flight: Flight) {
    this.messageService.add({
      severity: 'success',
      detail: 'Flight added to booking!',
      life: 3000,
    });

    this.router.navigate(['/payment'], {
      queryParams: { id: flight.flightNo, price: flight.price },
    });
  }
}
