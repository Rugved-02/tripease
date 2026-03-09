import { Component, OnInit, inject, signal } from '@angular/core';
import { CardModule } from 'primeng/card';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ChartModule } from 'primeng/chart';
import { AnalyticsService } from '../../core/services/analytics/analytics.service';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [
    CardModule,
    MatIconModule,
    CommonModule,
    ChartModule
  ],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css',
})
export class AnalyticsComponent implements OnInit {
  private dataservice = inject(AnalyticsService);

  // Data Signals
  dashboardStats = signal<any[]>([]);
  barData = signal<any>(null);
  lineData = signal<any>(null);
  pieData = signal<any>(null);
  insights = signal<any>({});

  // Option Signals (Using signals here allows for dynamic theme changes later if needed)
  barOptions = signal<any>(null);
  lineOptions = signal<any>(null);
  pieOptions = signal<any>(null);

  ngOnInit(): void {
    this.initChartOptions();
    this.loadAnalyticsData();
  }

  loadAnalyticsData(): void {
    this.dataservice.getDashboardData().subscribe({
      next: (data) => {
        // Use .set() to update signal values
        this.dashboardStats.set(data.stats);
        this.barData.set(data.barData);
        this.lineData.set(data.lineData);
        this.pieData.set(data.pieData);
        this.insights.set(data.insights);
      },
      error: (err) => {
        console.error('Could not load TripEase Analytics:', err);
      }
    });
  }

  initChartOptions() {
    this.barOptions.set({
      plugins: {
        legend: { position: 'bottom', labels: { usePointStyle: true, boxWidth: 8 } },
        tooltip: {
          backgroundColor: '#fff',
          titleColor: '#000',
          bodyColor: '#6366f1',
          borderWidth: 1,
          callbacks: { label: (ctx: any) => `Customers: ${ctx.raw}` }
        }
      },
      scales: {
        x: { grid: { display: false } },
        y: { beginAtZero: true, grid: { color: '#f1f5f9' } }
      }
    });

    this.lineOptions.set({
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: 'top',
          align: 'end',
          labels: { usePointStyle: true, boxWidth: 6 }
        },
        tooltip: { mode: 'index', intersect: false }
      },
      scales: {
        x: { grid: { display: false }, ticks: { color: '#94a3b8' } },
        y: {
          border: { display: false },
          grid: { color: '#f1f5f9' },
          ticks: { 
            color: '#94a3b8',
            callback: (value: number) => '$' + value.toLocaleString() 
          }
        }
      },
      elements: { point: { radius: 3, hoverRadius: 6 } }
    });

    this.pieOptions.set({
      plugins: {
        legend: { position: 'bottom', labels: { usePointStyle: true, padding: 20 } }
      },
      maintainAspectRatio: false
    });
  }
}