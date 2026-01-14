import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TimelineModule } from 'primeng/timeline';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
 
interface ItineraryEvent {
  status?: string;
  subtitle: string;
  title: string;
  time: string;
  color: string;
  iconType: 'flight' | 'hotel' | 'location';
}
 
@Component({
  selector: 'app-itinerary',
  standalone: true,
  imports: [CommonModule, TimelineModule, ButtonModule, CardModule, TagModule],
  templateUrl: './itinerary.html',
  styleUrl: './itinerary.css'
})
export class ItineraryComponent implements OnInit {
 
  events: ItineraryEvent[] = [];
 
  ngOnInit() {
    this.events = [
      {
        title: 'Flight Departure - DL 1234',
        subtitle: 'Departure from JFK to LAX',
        time: '08:00 AM',
        iconType: 'flight',
        color: '#3B82F6', // Blue
      },
      {
        title: 'Hotel Check-in',
        subtitle: 'Grand Plaza Hotel, Los Angeles',
        time: '11:30 AM',
        iconType: 'hotel',
        color: '#6366F1', // Indigo
      },
      {
        title: 'Business Meeting',
        subtitle: 'Downtown Convention Center',
        time: '03:00 PM',
        iconType: 'location',
        color: '#A855F7', // Purple
      }
    ];
  }
}