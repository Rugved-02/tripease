import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { DatePickerModule } from 'primeng/datepicker';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Router } from '@angular/router';
import { Hotel, HotelBookingService } from '../../core/services/hotel-booking/hotel-booking.service';

@Component({
  selector: 'app-hotel-booking',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    DatePickerModule,
    ButtonModule,
    InputTextModule
  ],
  templateUrl: './hotel-booking.component.html',
  styleUrl: './hotel-booking.component.css',
})
export class HotelBookingComponent implements OnInit {
  hotelForm!: FormGroup;
  availableHotels: any[] = [];

  private router = inject(Router);
  private hotelBookingService = inject(HotelBookingService);

  private allHotels = this.hotelBookingService.getHotels();

  private fb = inject(FormBuilder);

  ngOnInit(): void {
    this.hotelForm = this.fb.group({
      destination: ['', Validators.required],
      checkin: [null, Validators.required],
      checkout: [null, Validators.required]
    });
    this.availableHotels = [...this.allHotels];
  }

  getStars(count: number): number[] {
    return Array(Math.floor(count)).fill(0);
  }

  searchHotels() {
    const searchTerm = this.hotelForm.get('destination')?.value?.toLowerCase().trim();
    if (searchTerm) {
      this.availableHotels = this.allHotels.filter(hotel => 
        hotel.location.toLowerCase().includes(searchTerm) || 
        hotel.name.toLowerCase().includes(searchTerm)
      );
    } else {
      this.availableHotels = [...this.allHotels];
    }
  }

  onBook(hotel:Hotel){
    this.router.navigate(['/payment'], {queryParams: {id: hotel.id, price: hotel.price}});
  }

}
