import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
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
    CommonModule,FormsModule,ToastModule,FormsModule, SelectModule
  ],
  templateUrl: './flight-booking.component.html',
  styleUrls: ['./flight-booking.component.css'],
  providers: [MessageService] // PrimeNG Toast Service
})
export class FlightBookingComponent implements OnInit {
  
  // Data specifically focused on Delta Airlines per your request
  flights: Flight[] = [
    { airline: 'Delta Airlines', flightNo: 'DL 1234', class: 'Economy', depTime: '08:00 AM', depCity: 'New York (JFK)', arrTime: '11:30 AM', arrCity: 'Los Angeles (LAX)', duration: '5h 30m', price: 350, seats: 45 },
    { airline: 'United Airlines', flightNo: 'UA 5678', class: 'Business', depTime: '02:00 PM', depCity: 'New York (JFK)', arrTime: '05:30 PM', arrCity: 'Los Angeles (LAX)', duration: '5h 30m', price: 425, seats: 12 },
    { airline: 'American Airlines', flightNo: 'AA 9012', class: 'Economy', depTime: '07:00 PM', depCity: 'New York (JFK)', arrTime: '07:00 AM+1', arrCity: 'London (LHR)', duration: '7h 00m', price: 850, seats: 78 },
    { airline: 'Emirates', flightNo: 'EK 2345', class: 'First Class', depTime: '10:30 PM', depCity: 'New York (JFK)', arrTime: '08:00 PM+1', arrCity: 'Dubai (DXB)', duration: '12h 30m', price: 1250, seats: 8 }
  ];

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

  constructor(private messageService: MessageService) {}

  ngOnInit() {
    this.filteredFlights = this.flights;
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
      summary: 'Confirmed', 
      detail: 'Flight added to booking',
      life: 3000
    });
  }
}