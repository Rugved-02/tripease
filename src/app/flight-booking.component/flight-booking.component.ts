import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser'; // Add this import
interface Flight {
  airline: string;
  flightNo: string;
  class: string;
  depTime: string;
  depCity: string;
  arrTime: string;
  arrCity: string;
  duration: string;
  price: number;
  seats: number;
}

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
  flights: Flight[] = [
    { airline: 'Delta Airlines', flightNo: 'DL 1234', class: 'Economy', depTime: '08:00 AM', depCity: 'New York (JFK)', arrTime: '11:30 AM', arrCity: 'Los Angeles (LAX)', duration: '5h 30m', price: 350, seats: 45 },
    { airline: 'United Airlines', flightNo: 'UA 5678', class: 'Business', depTime: '02:00 PM', depCity: 'New York (JFK)', arrTime: '05:30 PM', arrCity: 'Los Angeles (LAX)', duration: '5h 30m', price: 425, seats: 12 },
    { airline: 'American Airlines', flightNo: 'AA 9012', class: 'Economy', depTime: '07:00 PM', depCity: 'New York (JFK)', arrTime: '07:00 AM+1', arrCity: 'London (LHR)', duration: '7h 00m', price: 850, seats: 78 },
    { airline: 'Emirates', flightNo: 'EK 2345', class: 'First Class', depTime: '10:30 PM', depCity: 'New York (JFK)', arrTime: '08:00 PM+1', arrCity: 'Dubai (DXB)', duration: '12h 30m', price: 1250, seats: 8 },
    { airline: 'Delta Airlines', flightNo: 'DL 101', class: 'Economy', depTime: '06:30 AM', depCity: 'Atlanta (ATL)', arrTime: '08:45 AM', arrCity: 'Miami (MIA)', duration: '2h 15m', price: 180, seats: 22 },
  { airline: 'United Airlines', flightNo: 'UA 242', class: 'Business', depTime: '09:15 AM', depCity: 'Chicago (ORD)', arrTime: '12:30 PM', arrCity: 'San Francisco (SFO)', duration: '5h 15m', price: 550, seats: 8 },
  { airline: 'American Airlines', flightNo: 'AA 880', class: 'Economy', depTime: '11:00 AM', depCity: 'Dallas (DFW)', arrTime: '01:20 PM', arrCity: 'New York (JFK)', duration: '3h 20m', price: 210, seats: 56 },
  { airline: 'Emirates', flightNo: 'EK 202', class: 'First Class', depTime: '11:00 PM', depCity: 'New York (JFK)', arrTime: '08:45 PM+1', arrCity: 'Dubai (DXB)', duration: '12h 45m', price: 4200, seats: 4 },
  { airline: 'Lufthansa', flightNo: 'LH 401', class: 'Business', depTime: '03:45 PM', depCity: 'Frankfurt (FRA)', arrTime: '06:15 PM', arrCity: 'New York (JFK)', duration: '8h 30m', price: 1850, seats: 15 },
  { airline: 'Qatar Airways', flightNo: 'QR 708', class: 'Economy', depTime: '01:30 AM', depCity: 'Washington (IAD)', arrTime: '11:00 PM', arrCity: 'Doha (DOH)', duration: '13h 30m', price: 920, seats: 110 },
  { airline: 'British Airways', flightNo: 'BA 175', class: 'Economy', depTime: '08:20 AM', depCity: 'London (LHR)', arrTime: '11:35 AM', arrCity: 'New York (JFK)', duration: '8h 15m', price: 680, seats: 42 },
  { airline: 'Singapore Airlines', flightNo: 'SQ 22', class: 'Business', depTime: '11:35 AM', depCity: 'Singapore (SIN)', arrTime: '06:00 PM', arrCity: 'Newark (EWR)', duration: '18h 25m', price: 3100, seats: 10 },
  { airline: 'Air France', flightNo: 'AF 006', class: 'First Class', depTime: '02:00 PM', depCity: 'Paris (CDG)', arrTime: '04:20 PM', arrCity: 'New York (JFK)', duration: '8h 20m', price: 5500, seats: 2 },
  { airline: 'JetBlue', flightNo: 'B6 523', class: 'Economy', depTime: '07:00 AM', depCity: 'Boston (BOS)', arrTime: '10:30 AM', arrCity: 'Orlando (MCO)', duration: '3h 30m', price: 145, seats: 30 },
  { airline: 'Southwest', flightNo: 'WN 128', class: 'Economy', depTime: '05:45 PM', depCity: 'Phoenix (PHX)', arrTime: '07:00 PM', arrCity: 'Las Vegas (LAS)', duration: '1h 15m', price: 89, seats: 18 },
  { airline: 'Cathay Pacific', flightNo: 'CX 841', class: 'Business', depTime: '10:00 AM', depCity: 'New York (JFK)', arrTime: '02:00 PM+1', arrCity: 'Hong Kong (HKG)', duration: '16h 00m', price: 2800, seats: 14 },
  { airline: 'Turkish Airlines', flightNo: 'TK 001', class: 'Economy', depTime: '01:45 PM', depCity: 'Istanbul (IST)', arrTime: '06:10 PM', arrCity: 'New York (JFK)', duration: '10h 25m', price: 740, seats: 85 },
  { airline: 'Delta Airlines', flightNo: 'DL 456', class: 'Business', depTime: '04:00 PM', depCity: 'Seattle (SEA)', arrTime: '09:30 PM', arrCity: 'Tokyo (HND)', duration: '10h 30m', price: 2100, seats: 6 },
  { airline: 'Alaska Airlines', flightNo: 'AS 712', class: 'Economy', depTime: '08:00 AM', depCity: 'Portland (PDX)', arrTime: '11:45 AM', arrCity: 'Chicago (ORD)', duration: '3h 45m', price: 320, seats: 25 },
  { airline: 'Virgin Atlantic', flightNo: 'VS 003', class: 'Economy', depTime: '12:30 PM', depCity: 'London (LHR)', arrTime: '03:40 PM', arrCity: 'New York (JFK)', duration: '8h 10m', price: 590, seats: 40 }
];
   onSearch(fromLoc: string, toLoc: string) {
  // 1. Convert search inputs to lowercase once
  const searchFrom = fromLoc.toLowerCase().trim();
  const searchTo = toLoc.toLowerCase().trim();

  this.filteredFlights = this.flights.filter(flight => {
    // 2. Convert flight data to lowercase for comparison
    const flightFrom = flight.depCity.toLowerCase();
    const flightTo = flight.arrCity.toLowerCase();
    
    // 3. Check if the flight city includes the search text
    const matchesFrom = flightFrom.includes(searchFrom);
    const matchesTo = flightTo.includes(searchTo);
    
    return matchesFrom && matchesTo;
  });

  // 4. Feedback for the user
  if (this.filteredFlights.length === 0) {
    this.messageService.add({
      severity: 'warn', 
      summary: 'No Flights Found', 
      life: 3000
    });
  }
}
  filteredFlights: Flight[] = [];
  selectedFilter: string = 'All';

  // PrimeNG Select Options
  passengerOptions = [
    { label: '1 Adult', value: '1 Adult' },
    { label: '2 Adults', value: '2 Adults' },
    { label: '3 Adults', value: '3 Adults' },
    { label: '4+ Adults', value: '4+ Adults' },
    
  ];
  selectedPassenger: string = '1 Adult';

  constructor(private messageService: MessageService,
    
    private sanitizer:DomSanitizer
  ) {}

  ngOnInit() {
    this.filteredFlights = this.flights;
  }
  getSafeIcon(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.flightIconSvg);
  }
  // Filter Logic
  filterByClass(flightClass: string) {
    this.selectedFilter = flightClass;
    if (flightClass === 'All') {
      this.filteredFlights = this.flights;
    } else {
      this.filteredFlights = this.flights.filter(f => f.class === flightClass);
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