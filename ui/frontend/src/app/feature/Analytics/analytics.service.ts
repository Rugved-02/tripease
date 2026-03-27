import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {
      getStats() {
        return [
          { label: 'Total Revenue', value: '$328,000', icon: 'attach_money', trend: '+12.5%', colorClass: 'revenue-bg' },
          { label: 'Total Customers', value: '3,890', icon: 'group', trend: '+8.3%', colorClass: 'customers-bg' },
          { label: 'Flight Bookings', value: '1,250', icon: 'flight', trend: '-15.2%', colorClass: 'flights-bg' },
          { label: 'Hotel Bookings', value: '890', icon: 'domain', trend: '+6.7%', colorClass: 'hotels-bg' }
        ];
      }

      getBarData() {
        return {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'customers',
            data: [2100, 2450, 2780, 3100, 3550, 4000],
            backgroundColor: '#6366f1',
            borderRadius: 8,
            barThickness: 40
          }]
        };
      }

      getLineData() {
        return {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'Amount',
            data: [45000, 52000, 48000, 62000, 56000, 68000],
            borderColor: '#3b82f6',
            tension: 0.4,
            pointHitRadius: 50,     
            pointHoverRadius: 8,    
            pointRadius: 3
          }]
        };
      }

      getPieData() {
        return {
          labels: ['Flights', 'Hotels', 'Itinerary'],
          datasets: [{
            data: [48, 34, 18],
            backgroundColor: ['#3b82f6', '#6366f1', '#a855f7'],
            radius: '70%'
          }]
        };
      }
}
