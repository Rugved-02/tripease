import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { CommonModule, NgIf } from '@angular/common';
import { MenuItem } from 'primeng/api';
import { HttpClient } from '@angular/common/http';
import { DashboardService } from '../../core/services/dashboard/dashboard.service';
import { sign } from 'crypto';
import { RecentBookingsResponseDTO } from '../../core/services/dashboard/dto/RecentBookingsResponseDTO';
import { DashboardStatsResponseDTO } from '../../core/services/dashboard/dto/DashboardStatsResponseDTO';
import { Router, RouterLink } from '@angular/router';
import { BehaviorSubject, Observable, scan, switchMap, tap } from 'rxjs';
import { SliceResponseRecentBookingsDTO } from '../../core/services/dashboard/dto/SliceResponseRecentBookingsDTO';
import { DrawerModule } from 'primeng/drawer';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { response } from 'express';
import { error } from 'console';
import { UserResponseDTO } from '../../core/services/dashboard/dto/UserResponseDTO';

@Component({
  selector: 'app-dashboard',
  imports: [
    ButtonModule,
    DialogModule,
    DrawerModule,
    CommonModule,
    PanelModule,
    CardModule,
    MenuModule,
    NgIf,
    RouterLink,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {

  private router = inject(Router);
  visible = false;

  stats = signal<DashboardStatsResponseDTO | null>(null);
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

    this.visible = false;
    this.loadUserProfileDetails();
    this.loadDashboardStats();
    this.loadRecentBookings();
    this.loadLatestTrip();
  }

  userProfileDetails = signal<UserResponseDTO | null>(null);
  
    loadUserProfileDetails(): void {
      this.dashboardService.getLoggedInUserDetails().subscribe({
        next: (response)=> {
          this.userProfileDetails.set(response);
          console.log(response);
        },
        error: (err) => console.log(err)
      })
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
      },
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
          this.recentBookings.update((prev) => [...prev, ...data.content]);
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
      },
    });
  }

  latestTrip = signal<any>(null);
  loadLatestTrip() {
    this.dashboardService.getLatestTrip().subscribe({
      next: (res) => {
        console.log(res);
        this.latestTrip.set(res);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  // 2. The UI Panel automatically re-calculates whenever 'stats' changes
  dataFeaturesPanel = computed(() => {
    const s = this.stats(); // Track dependency

    return [
      {
        icon: 'pi pi-calendar',
        title: s?.totalBookings ?? '0',
        description: 'Total Bookings',
        dynamicColor: 'var(--p-blue-600)',
      },
      {
        icon: 'pi pi-check-circle',
        title: s?.confirmedBookings ?? '0',
        description: 'Confirmed',
        dynamicColor: 'var(--p-green-600)',
      },
      {
        icon: 'pi pi-clock',
        title: s?.pendingBookings ?? '0',
        description: 'Pending',
        dynamicColor: 'var(--p-yellow-500)',
      },
      {
        icon: 'pi pi-clock',
        title: s?.cancelledBookings ?? '0',
        description: 'Cancelled',
        dynamicColor: 'var(--p-slate-500)',
      },
      {
        icon: 'pi pi-credit-card',
        title: s ? s.totalSpent : '0.00',
        description: 'Total Spent',
        dynamicColor: 'var(--p-purple-500)',
      },
    ];
  });

  quickActions = signal([
    {
      icon: 'pi pi-send',
      label: 'Book Flight',
      link: '/bookFlight',
    },
    {
      icon: 'pi pi-building',
      label: 'Reserve Hotel',
      link: '/bookHotel',
    },
    {
      icon: 'pi pi-calendar',
      label: 'View Itinerary',
      link: '/itineraryPlanning',
    },
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
    return this.recentBookings().map((booking) => {
      const isHotel = booking.resourceType === 'HOTEL';
      const details = booking.resourceDetails;

      return {
        bookingDTO: booking, // Pass the whole object or just the fields below
        isHotel,
        resourceType: booking.resourceType,
        startDate: booking.startDate,
        endDate: booking.endDate,
        price: booking.totalAmount,
        status: booking.bookingStatus,
        details: booking.resourceDetails, // assuming this contains hotelName, airline, etc.
      };
    });
  });

  upcomingTrip = computed(() => {
    const trip = this.latestTrip();

    // If trip is null or undefined, return the fallback content
    if (!trip) {
      return {
        title: 'No Trips Found',
        message: 'Create a trip in the itinerary section to get started!',
      };
    }

    // If trip exists, return the reminder content
    return {
      title: 'Upcoming Trip Reminder',
      message: `Your ${trip.tripName} is scheduled on ${trip.startDate}. Make sure all arrangements are confirmed!`,
    };
  });
  dialogRecentBookingData = signal<any>(null);

  onRecentBookingClick(bookingDTO: any) {
    this.dialogRecentBookingData.set(bookingDTO);
    // this.dashboardService;
    this.visible = true;
  }

  navigateTo(type: string){
    if(type === 'hotel'){
      this.router.navigate(['/bookHotel']);
    }
    else{
      this.router.navigate(['/bookFlight']);
    }
  }
}

interface UserDataDashboard {
  icon: string;
  title: string;
  description: string;
  dynamicColor: string;
}
