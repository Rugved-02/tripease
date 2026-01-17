import { Component } from '@angular/core';
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

@Component({
  selector: 'app-search-flight-hotels-homepage',
  imports: [FormsModule,SelectButton,DatePickerModule, ReactiveFormsModule, InputTextModule, ButtonModule, ToastModule, MessageModule, TabsModule, CommonModule],
  providers: [MessageService],
  templateUrl: './search-flight-hotels-homepage.component.html',
  styleUrl: './search-flight-hotels-homepage.component.css',
})
export class SearchFlightHotelsHomepageComponent {

  stateOptions: any[] = [
    { label: 'Flights', value: 'flights' },
    { label: 'Hotels', value: 'hotels' }
  ];

  selectedTab: string = 'flights';

  exampleForm: FormGroup = new FormGroup({
    fromCity: new FormControl('', Validators.required),
    toCity: new FormControl('', Validators.required),
    selectedDate: new FormControl('')
  });

  isInvalid(x: String){
    return false;
  }
  
  onSubmit(){
    console.log("on submit exec searchflight")
  }

}
