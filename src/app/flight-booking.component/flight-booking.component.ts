import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser'; // Add this import
import { Flights, Flight } from '../flights';

@Component({
  selector: 'app-flight-booking',
  imports: [
    CommonModule,FormsModule,ToastModule,FormsModule, SelectModule, InputTextModule
  ],
  templateUrl: './flight-booking.component.html',
  styleUrls: ['./flight-booking.component.css'],
  providers: [MessageService] // PrimeNG Toast Service
})
export class FlightBookingComponent implements OnInit {
  flightIconSvg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <path d="M17.8 19.2 16 11l3.5-3.5C21 6 21.5 4 21 3c-1-.5-3 0-4.5 1.5L13 8 4.8 6.2c-.5-.1-.9.1-1.1.5l-.3.5c-.2.5-.1 1 .3 1.3L9 12l-2 3H4l-1 1 3 2 2 3 1-1v-3l3-2 3.5 5.3c.3.4.8.5 1.3.3l.5-.2c.4-.3.6-.7.5-1.2z"></path>
    </svg>`;
  // Data specifically focused on Delta Airlines per your request
  allFlights: Flight[] = [];
  filteredFlights: Flight[] = [];
  selectedFilter: string = 'All';
  selectedPassenger: string = '1 Adult';
   passengerOptions = [
    { label: '1 Adult', value: '1 Adult' },
    { label: '2 Adults', value: '2 Adults' },
    { label: '3 Adults', value: '3 Adults' },
    { label: '4+ Adults', value: '4+ Adults' },
    
  ];
  
  constructor(private messageService: MessageService,
    private flightService: Flights,
    private sanitizer:DomSanitizer
  ) {}

  ngOnInit() {
    this.allFlights=this.flightService.getFlights();
    this.filteredFlights = this.allFlights;
  }
  getSafeIcon(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.flightIconSvg);
  }
   onSearch(fromLoc: string, toLoc: string) {
  const searchFrom = fromLoc.toLowerCase().trim();
  const searchTo = toLoc.toLowerCase().trim();

  this.filteredFlights = this.allFlights.filter(flight => {
    return flight.depCity.toLowerCase().includes(searchFrom) && 
             flight.arrCity.toLowerCase().includes(searchTo);
  });

  // Feedback for the user
  if (this.filteredFlights.length === 0) {
    this.messageService.add({
      severity: 'warn', 
      summary: 'No Flights Found', 
      life: 3000
    });
  }
}

  // Filter Logic
  filterByClass(flightClass: string) {
    this.selectedFilter = flightClass;
    if (flightClass === 'All') {
      this.filteredFlights = this.allFlights;
    } else {
      this.filteredFlights = this.allFlights.filter(f => f.class === flightClass);
    }
  }

  // Toast notification for "Book Now"
  onBook() {
    this.messageService.add({
      severity: 'success', 
      detail: 'Flight added to booking!',
      life: 3000
    });
  }
}