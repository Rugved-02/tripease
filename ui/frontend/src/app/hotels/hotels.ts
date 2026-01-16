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













/*import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { HotelService } from './hotel.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-hotels',
  templateUrl: './hotels.html',
  styleUrls: ['./hotels.css'],
  providers: [MessageService]
})
export class Hotels {
  hotelForm: FormGroup;
  availableHotels: any[] = [];
  loading: boolean = false;
  submitted: boolean = false;

  constructor(private fb: FormBuilder, private hotelService: HotelService) {
    this.hotelForm = this.fb.group({
      destination: ['', [Validators.required, Validators.minLength(3)]],
      checkin: [null, Validators.required],
      checkout: [null, Validators.required]
    }, { validators: this.dateLessThan('checkin', 'checkout') });
  }

  // Custom Validator for Date Range
  dateLessThan(from: string, to: string) {
    return (group: AbstractControl): ValidationErrors | null => {
      const start = group.get(from)?.value;
      const end = group.get(to)?.value;
      return start && end && start >= end ? { dateRange: true } : null;
    };
  }

  searchHotels() {
    this.submitted = true;
    if (this.hotelForm.invalid) return;

    this.loading = true;
    const { destination } = this.hotelForm.value;

    this.hotelService.searchHotels(destination).subscribe({
      next: (data) => {
        this.availableHotels = data;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }
}*/

//Hotel.service

// import { Injectable } from '@angular/core';
// import { HttpClient, HttpParams } from '@angular/common/http';
// import { Observable, delay } from 'rxjs';

// @Injectable({ providedIn: 'root' })
// export class HotelService {
//   private apiUrl = 'http://localhost:3000/hotels';

//   constructor(private http: HttpClient) {}

//   searchHotels(location: string): Observable<any[]> {
//     // JSON Server allows filtering using query params (e.g., ?location=Dubai)
//     const params = new HttpParams().set('location_like', location); 
//     return this.http.get<any[]>(this.apiUrl, { params }).pipe(delay(800)); // Added delay to simulate network
//   }
// }
