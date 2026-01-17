import { Component, OnInit, signal,inject } from '@angular/core';
import { MenubarComponent } from '../../../shared/components/menubar.component/menubar.component';
import { CardModule } from 'primeng/card';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ChartModule } from 'primeng/chart'
import { AnalyticsService } from '../analytics.service';

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

        private dataservice = inject(AnalyticsService)
        
        dashboardStats: any[] = [];
        barData: any;
        barOptions: any;
        lineData: any;
        lineOptions:any;
        pieData: any;
        pieOptions: any;

        ngOnInit(): void {
          this.dashboardStats = this.dataservice.getStats();
          this.barData = this.dataservice.getBarData();
          this.lineData = this.dataservice.getLineData();
          this.pieData = this.dataservice.getPieData();


          this.initChartOptions();

        }

        initChartOptions(){
          this.barOptions = {
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
          };

          // 2. Line Options (New)
          this.lineOptions = {
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
              x: {
                grid: { display: false },
                ticks: { color: '#94a3b8' }
              },
              y: {
                border: { display: false },
                grid: { color: '#f1f5f9' },
                ticks: { 
                  color: '#94a3b8',
                  callback: (value: number) => '$' + value.toLocaleString() 
                }
              }
            },
            elements: {
              point: { radius: 3, hoverRadius: 6}
            }
          };

          // 3. Pie Options
          this.pieOptions = {
            plugins: {
              legend: { position: 'bottom', labels: { usePointStyle: true, padding: 20 } }
            },
            maintainAspectRatio: false
          };
        }
        
}
