import { Component, signal } from '@angular/core';

import { ItineraryComponent } from "./itinerary/itinerary";



@Component({
  selector: 'app-root',
  imports: [ItineraryComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
 
     
}
