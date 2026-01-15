import { Component, OnInit, signal } from '@angular/core';
import { MenubarComponent } from '../../../shared/components/menubar.component/menubar.component';
import { CardModule } from 'primeng/card';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ChartModule } from 'primeng/chart'

@Component({
  selector: 'app-analytics',
  imports: [MenubarComponent,
            CardModule,
            MatIcon,
            MatIconModule,
            CommonModule,
            ChartModule
  ],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css',
})
export class AnalyticsComponent implements OnInit {
        barData: any;
        barOptions: any;
        dashboardStats = [
          { label: 'Total Revenue', value: '$328,000', icon: 'attach_money', trend: '+12.5%', colorClass: 'revenue-bg' },
          { label: 'Total Customers', value: '3,890', icon: 'group', trend: '+8.3%', colorClass: 'customers-bg' },
          { label: 'Flight Bookings', value: '1,250', icon: 'flight', trend: '+15.2%', colorClass: 'flights-bg' },
          { label: 'Hotel Bookings', value: '890', icon: 'domain', trend: '+6.7%', colorClass: 'hotels-bg' }
        ];

        lineData = {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'Amount',
            data: [45000, 52000, 48000, 62000, 56000, 68000],
            fill: false,
            borderColor: '#3b82f6',
            tension: 0.4
          }]
        };

        pieData = {
          labels: ['Flights', 'Hotels', 'Itinerary'],
          datasets: [{
            data: [48, 34, 18],
            backgroundColor: ['#3b82f6', '#6366f1', '#a855f7'],
            // 70% makes the pie smaller without shrinking the card or legend
            radius: '70%' 
          }]
        };

        pieOptions = {
          plugins: {
            legend: {
              position: 'bottom',
              labels: {
                usePointStyle: true,
                padding: 20
              }
            }
          },
          // Setting this to false prevents the chart from forced stretching
          maintainAspectRatio: false 
        };
        ngOnInit(): void {
         this.barData = {
              labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
              datasets: [
                  {
                      label: 'customers',
                      data: [2100, 2450, 2780, 3100, 3550, 4000],
                      backgroundColor: '#6366f1', // The Indigo/Purple color from your UI
                      borderRadius: 8,           // Rounded bar tops
                      borderSkipped: false,
                      barThickness: 40           // Adjust thickness to match screenshot
                  }
              ]
          };
          this.barOptions = {
                  plugins: {
                      legend: {
                          display: true,
                          position: 'bottom',
                          labels: { usePointStyle: true, boxWidth: 8 }
                      },
                      tooltip: {
                          backgroundColor: '#fff',
                          titleColor: '#000',
                          bodyColor: '#6366f1',
                          borderColor: '#e2e8f0',
                          borderWidth: 1,
                          padding: 12,
                          displayColors: false,
                          callbacks: {
                              label: (context: any) => `customers : ${context.raw}`
                          }
                      }
                  },
                  scales: {
                      x: {
                          grid: { display: false } // No vertical lines
                      },
                      y: {
                          beginAtZero: true,
                          ticks: { stepSize: 1000 },
                          grid: {
                              color: '#f1f5f9', // Light horizontal lines
                              drawBorder: false
                          }
                      }
                  }
              };

        }
}
