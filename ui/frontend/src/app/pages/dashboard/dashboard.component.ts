import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { CommonModule, NgIf } from '@angular/common';
import { MenuItem } from 'primeng/api';
import { HttpClient } from '@angular/common/http';
import { DashboardService} from '../../core/services/dashboard/dashboard.service';
import { sign } from 'crypto';
import { RecentBookingsResponseDTO } from '../../core/services/dashboard/dto/RecentBookingsResponseDTO';
import { DashboardStatsResponseDTO } from '../../core/services/dashboard/dto/DashboardStatsResponseDTO';
import { RouterLink } from "@angular/router";
import { BehaviorSubject, Observable, scan, switchMap, tap } from 'rxjs';
import { SliceResponseRecentBookingsDTO } from '../../core/services/dashboard/dto/SliceResponseRecentBookingsDTO';




@Component({
  selector: 'app-dashboard',
  imports: [ButtonModule, CommonModule, PanelModule, CardModule, MenuModule, NgIf, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {

  stats = signal<DashboardStatsResponseDTO | null>(null);
  // recentBookings = signal<RecentBookingsResponseDTO[]>([]);
  isLoading = true;
  errorMessage = '';

  // Existing signal
recentBookings = signal<RecentBookingsResponseDTO[]>([]);
currentPage = signal<number>(0);
isLastPage = signal<boolean>(false);
isLoadingRecentBookings = signal<boolean>(false);

  private page$ = new BehaviorSubject<number>(0);
  private pageSize = 3; // Load 3 at a time
  
  // // Data stream
  // bookings$: Observable<RecentBookingsResponseDTO[]> | undefined;
  // isLastPage = false;
  // isLoadingRecentBookings = false;

  dashboardService = inject(DashboardService);

  ngOnInit(): void {
    // // it fetches for particular user but the controller in backend 
    // // directly takes the userId from headers attached by API gateway in every call
    // this.bookings$ = this.page$.pipe(
    //   tap(() => this.isLoading = true),
    //   // switchMap handles the API call
    //   switchMap(page => this.dashboardService.getRecentBookings(page, this.pageSize)),
    //   tap(response => {
    //     this.isLastPage = response.last;
    //     this.isLoading = false;
    //   }),
    //   // Accumulate results: [Old Data] + [New Data]
    //   scan((acc, response) => [...acc, ...response.content], [] as RecentBookingsResponseDTO[])
    // );
  
    this.loadDashboardStats();
    this.loadRecentBookings();
  }

  loadMore() {
    if (!this.isLastPage && !this.isLoading) {
      const nextPage = this.page$.value + 1;
      this.page$.next(nextPage);
    }
  }

  loadDashboardStats(): void {
    this.dashboardService.getUserStats().subscribe({
      next: (data: any) => {
        this.stats.set(data);
        this.isLoading = false;
      },
      error: (err: any) => {
        this.errorMessage = 'Failed to load dashboard statistics.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  loadRecentBookings(isLoadMore: boolean = false): void {
  this.isLoadingRecentBookings.set(true);
  
  // If loading more, increment page; otherwise start at 0
  const pageToLoad = isLoadMore ? this.currentPage() + 1 : 0;
  const pageSize = isLoadMore ? 3 : 5; // Initial 5, then 3 more

  this.dashboardService.getRecentBookings(pageToLoad, pageSize).subscribe({
    next: (data: SliceResponseRecentBookingsDTO<RecentBookingsResponseDTO>) => {
      if (isLoadMore) {
        // Append data to the existing signal array
        this.recentBookings.update(prev => [...prev, ...data.content]);
        this.currentPage.set(pageToLoad);
      } else {
        // First load: just set the data
        this.recentBookings.set(data.content);
        this.currentPage.set(0);
      }
      
      this.isLastPage.set(data.last);
      this.isLoadingRecentBookings.set(false);
    },
    error: (err) => {
      this.errorMessage = 'Failed to load recent bookings.';
      this.isLoadingRecentBookings.set(false);
      console.error(err);
    }
  });
}

  // loadRecentBookings():void{
  //   this.dashboardService.getRecentBookings().subscribe({
  //     next: (data: any) => {
  //       this.recentBookings.set(data);
  //     },
  //     error: (err: any) => {
  //       this.errorMessage = 'Failed to load recent bookings.';
  //       this.isLoading = false;
  //       console.error(err);
  //     }
  //   })
  // }
  


// 2. The UI Panel automatically re-calculates whenever 'stats' changes
dataFeaturesPanel = computed(() => {
  const s = this.stats(); // Track dependency

  return [
    { 
      icon: "pi pi-calendar", 
      title: s?.totalBookings ?? '0', 
      description: "Total Bookings", 
      dynamicColor: "var(--p-blue-600)" 
    },
    { 
      icon: "pi pi-check-circle", 
      title: s?.confirmedBookings ?? '0', 
      description: "Confirmed", 
      dynamicColor: "var(--p-green-600)" 
    },
    { 
      icon: "pi pi-clock", 
      title: s?.pendingBookings ?? '0', 
      description: "Pending", 
      dynamicColor: "var(--p-yellow-500)" 
    },
    { 
      icon: "pi pi-clock", 
      title: s?.cancelledBookings ?? '0', 
      description: "Cancelled", 
      dynamicColor: "var(--p-slate-500)" 
    },
    { 
      icon: "pi pi-credit-card", 
      title: s ? s.totalSpent: '0.00', 
      description: "Total Spent", 
      dynamicColor: "var(--p-purple-500)" 
    }
  ];
});


quickActions = signal([
 {
   icon: 'pi pi-send',
   label: 'Book Flight',
   link: '/bookFlight'
 },
 {
   icon: 'pi pi-building',
   label: 'Reserve Hotel',
   link: '/bookHotel'
 },
 {
   icon: 'pi pi-calendar',
   label: 'View Itinerary',
   link: '/itineraryPlanning'
 }
]);

// recentBookingsPanel = computed(() => {
//   return this.recentBookings().map(booking => {
//     const isHotel = booking.resourceType === 'HOTEL';
//     const details = booking.resourceDetails;

//     return {
//       // Use PrimeIcons based on type
//       icon: isHotel ? 'pi pi-building' : 'pi pi-send',
      
//       // Build a dynamic title based on the resource type
//       title: isHotel 
//         ? `${details.hotelName} - ${details.hotelLocation}`
//         : `${details.airline} - ${details.flightNo} | ${details.depPlace} → ${details.arrPlace}`,
      
//       // Format the date (Angular Date Pipe can also do this in HTML)
//       date: new Date(booking.startDate).toLocaleDateString('en-US', { 
//         month: 'short', day: 'numeric', year: 'numeric' 
//       }),
      
//       price: booking.totalAmount,
//       status: booking.bookingStatus
//     };
//   });
// });


// Your existing computed logic stays almost exactly the same!
recentBookingsPanel = computed(() => {
  return this.recentBookings().map(booking => {
    const isHotel = booking.resourceType === 'HOTEL';
    const details = booking.resourceDetails;

    return {
      icon: isHotel ? 'pi pi-building' : 'pi pi-send',
      title: isHotel 
        ? `${details.hotelName} - ${details.hotelLocation}`
        : `${details.airline} - ${details.flightNo} | ${details.depPlace} → ${details.arrPlace}`,
      startDate: new Date(booking.startDate).toLocaleDateString('en-US', { 
        month: 'short', day: 'numeric', year: 'numeric' 
      }),
      endDate: new Date(booking.endDate || "").toLocaleDateString('en-US', { 
        month: 'short', day: 'numeric', year: 'numeric' 
      }),
      resourceType: booking.resourceType,
      createdAt: booking.createdAt,
      bookingDate: booking.updatedAt,
      price: booking.totalAmount,
      status: booking.bookingStatus
    };
  });
});

// recentBookingsPanel = signal([
//  {
//    icon: 'pi pi-send',
//    title: 'Delta Airlines - DL 1234 | JFK → LAX',
//    date: 'Feb 15, 2026',
//    price: '$350',
//    status: 'Confirmed'
//  },
//  {
//    icon: 'pi pi-building',
//    title: 'Grand Plaza Hotel - Los Angeles | 3 nights',
//    date: 'Feb 15, 2026',
//    price: '$750',
//    status: 'Confirmed'
//  },
//  {
//    icon: 'pi pi-send',
//    title: 'United Airlines - UA 5678 | LAX → JFK',
//    date: 'Feb 20, 2026',
//    price: '$425',
//    status: 'Pending'
//  }
// ]);


upcomingTrip = {

  title: 'Upcoming Trip Reminder',

  message: 'Your Los Angeles Business Trip is coming up in 15 days.                                  Make sure all arrangements are confirmed!'

};


}

interface UserDataDashboard {
  icon: string;
  title: string;
  description: string;
  dynamicColor: string;
}
