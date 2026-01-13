import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { CardModule } from 'primeng/card';
import { DatePickerModule } from 'primeng/datepicker';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-hotels',
  standalone: true,
  templateUrl: './hotels.html',
  styleUrls: ['./hotels.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    DatePickerModule,
    ButtonModule,
    InputTextModule
  ]
})
export class Hotels {
  hotelForm!: FormGroup;

  // Added 3 more cards to show 4 total hotels
  availableHotels = [
    { 
      name: 'Grand Plaza Hotel', 
      location: 'Los Angeles, CA', 
      image: 'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=500', 
      price: 250, 
      rating: 4.8,
      reviews: 1245,
      roomsLeft: 15
    },
    { 
      name: 'The Ritz-Carlton', 
      location: 'Dubai, UAE', 
      image: 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=500', 
      price: 450, 
      rating: 4.9,
      reviews: 3210,
      roomsLeft: 5
    },
    { 
      name: 'Blue Lagoon Resort', 
      location: 'Maldives', 
      image: 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=500', 
      price: 600, 
      rating: 5.0,
      reviews: 980,
      roomsLeft: 2
    },
    { 
      name: 'Mountain Peak Lodge', 
      location: 'Aspen, CO', 
      image: 'https://images.unsplash.com/photo-1518733057094-95b53143d2a7?auto=format&fit=crop&w=500', 
      price: 310, 
      rating: 4.7,
      reviews: 750,
      roomsLeft: 10
    }
  ];

  constructor(private fb: FormBuilder) {
    this.hotelForm = this.fb.group({
      destination: ['', Validators.required],
      checkin: [null, Validators.required],
      checkout: [null, Validators.required]
    });
  }

  searchHotels() {
    if (this.hotelForm.valid) {
      console.log('Form submitted:', this.hotelForm.value);
    }
  }
}