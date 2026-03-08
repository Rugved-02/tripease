import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormsModule, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

// PrimeNG Imports
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageModule } from 'primeng/message';
import { MessageService } from 'primeng/api';
import { DatePickerModule } from 'primeng/datepicker';
import { TabsModule } from 'primeng/tabs';

@Component({
  selector: 'app-search-flight-hotels-homepage',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    ReactiveFormsModule, 
    InputTextModule, 
    ButtonModule, 
    ToastModule, 
    MessageModule, 
    DatePickerModule, 
    TabsModule
  ],
  providers: [MessageService],
  templateUrl: './search-flight-hotels-homepage.component.html',
  styleUrl: './search-flight-hotels-homepage.component.css',
})
export class SearchFlightHotelsHomepageComponent {
  private router = inject(Router);

  activeTab: 'flights' | 'hotels' = 'flights';

  // Flight Form Definition
  searchFlights = new FormGroup({
    fromCity: new FormControl('', [Validators.required, Validators.minLength(2)]),
    toCity: new FormControl('', [Validators.required, Validators.minLength(2)]),
    selectedDate: new FormControl<Date | null>(null, Validators.required)
  });

  // Hotel Form Definition
  searchHotels = new FormGroup({
    toCity: new FormControl('', [Validators.required, Validators.minLength(2)]),
    checkInDate: new FormControl<Date | null>(null, Validators.required),
    checkOutDate: new FormControl<Date | null>(null, Validators.required)
  });

  setTab(tab: 'flights' | 'hotels') {
    this.activeTab = tab;
  }

  // Improved Validation Check
  isInvalid(controlName: string): boolean {
    
  const form = (this.activeTab === 'flights' ? this.searchFlights : this.searchHotels) as FormGroup;
  
  const control = form.get(controlName);
  return !!(control && control.invalid && (control.dirty || control.touched));
}

  onSubmitSearchFlights() {
    if (this.searchFlights.valid) {
      const formVal = this.searchFlights.value;
      this.router.navigate(['/bookFlight'], {
        queryParams: {
          from: formVal.fromCity,
          to: formVal.toCity,
          date: this.formatDate(formVal.selectedDate)
        }
      });
    } else {
      this.searchFlights.markAllAsTouched();
    }
  }

  onSubmitSearchHotels() {
    if (this.searchHotels.valid) {
      const formVal = this.searchHotels.value;
      this.router.navigate(['/bookHotel'], {
        queryParams: {
          city: formVal.toCity,
          checkInDate: this.formatDate(formVal.checkInDate),
          checkOutDate: this.formatDate(formVal.checkOutDate)
        }
      });
    } else {
      this.searchHotels.markAllAsTouched();
    }
  }

  // Helper to format date to YYYY-MM-DD for URL query params
  // Update this in search-flight-hotels-homepage.component.ts
private formatDate(date: any): string {
  if (!date) return '';
  
  const d = new Date(date);
  const year = d.getFullYear();
  // Months are 0-indexed, so we add 1 and pad with a leading zero if needed
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  
  return `${year}-${month}-${day}`; // Returns "YYYY-MM-DD" in local time
}
}