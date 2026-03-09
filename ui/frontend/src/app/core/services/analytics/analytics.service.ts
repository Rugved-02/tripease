import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment.development';

// Matching your Backend DTO structure
export interface AnalyticsDTO {
  totalRevenue: number;
  totalCustomers: number;
  flightBookings: number;
  hotelBookings: number;
  revenueTrend: number[];
  bookingsDistribution: { [key: string]: number };
  customerGrowth: number[];
  peakBookingSeason: string;
  averageBookingValue: number;
  customerRetention: string;
  revenueTrendPercentage: string;
  customerTrendPercentage: string;
}

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {

  private readonly API_URL = `${environment.gatewayUrl}/analytics/details`;

  constructor(private http: HttpClient) {}

  /**
   * Fetches the full analytics object and transforms it for the UI
   */
  getDashboardData(): Observable<any> {
    return this.http.get<AnalyticsDTO>(this.API_URL).pipe(
      map(data => ({
        // 1. Stats Cards Mapping
        stats: [
          { label: 'Total Revenue', value: this.formatCurrency(data.totalRevenue), icon: 'attach_money', trend: data.revenueTrendPercentage, colorClass: 'revenue-bg' },
          { label: 'Total Customers', value: data.totalCustomers.toLocaleString(), icon: 'group', trend: data.customerTrendPercentage, colorClass: 'customers-bg' },
          { label: 'Flight Bookings', value: data.flightBookings.toLocaleString(), icon: 'flight', trend: '+0.0%', colorClass: 'flights-bg' }, // Trend calculation can be added to DTO
          { label: 'Hotel Bookings', value: data.hotelBookings.toLocaleString(), icon: 'domain', trend: '+0.0%', colorClass: 'hotels-bg' }
        ],

        // 2. Bar Chart (Customer Growth)
        barData: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'customers',
            data: data.customerGrowth,
            backgroundColor: '#6366f1',
            borderRadius: 8,
            barThickness: 40
          }]
        },

        // 3. Line Chart (Revenue Trend)
        lineData: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'Amount',
            data: data.revenueTrend,
            borderColor: '#3b82f6',
            tension: 0.4,
            pointRadius: 3
          }]
        },

        // 4. Pie Chart (Bookings Distribution)
        pieData: {
          labels: Object.keys(data.bookingsDistribution),
          datasets: [{
            data: Object.values(data.bookingsDistribution),
            backgroundColor: ['#3b82f6', '#6366f1', '#a855f7'],
            radius: '70%'
          }]
        },

        // 5. Bottom Insights Cards
        insights: {
          peakSeason: data.peakBookingSeason,
          avgValue: this.formatCurrency(data.averageBookingValue),
          retention: data.customerRetention
        }
      }))
    );
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(value);
  }
}
 