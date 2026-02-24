import { Component, inject } from '@angular/core';
import { TabsModule } from 'primeng/tabs';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { MessageModule } from 'primeng/message';
import { MessageService } from 'primeng/api';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectButton } from 'primeng/selectbutton';
import { FormBuilder,FormsModule, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search-flight-hotels-homepage',
  imports: [FormsModule,SelectButton,DatePickerModule, ReactiveFormsModule, InputTextModule, ButtonModule, ToastModule, MessageModule, TabsModule, CommonModule],
  providers: [MessageService],
  templateUrl: './search-flight-hotels-homepage.component.html',
  styleUrl: './search-flight-hotels-homepage.component.css',
})
export class SearchFlightHotelsHomepageComponent {

  private router = inject(Router);

  activeTab: 'flights' | 'hotels' = 'flights';

  setTab(tab: 'flights' | 'hotels') {
    this.activeTab = tab;
  }

  stateOptions: any[] = [
    { label: 'Flights', value: 'flights' },
    { label: 'Hotels', value: 'hotels' }
  ];

  selectedTab: string = 'flights';

  searchFlights: FormGroup = new FormGroup({
    fromCity: new FormControl('', Validators.required),
    toCity: new FormControl('', Validators.required),
    selectedDate: new FormControl('')
  });

  searchHotels: FormGroup = new FormGroup({
    toCity: new FormControl('', Validators.required),
    selectedDate: new FormControl('')
  });

  isInvalid(x: String){
    return false;
  }
  onSubmitSearchFlights(){
    this.router.navigate(['/bookFlight']);
  }
  onSubmitSearchHotels(){
    console.log("searchhotel exec");
    this.router.navigate(['/bookHotel']);
  }

}
