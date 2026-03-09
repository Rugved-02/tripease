import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import { Observable } from 'rxjs';
import { DashboardStatsResponseDTO } from './dto/DashboardStatsResponseDTO';
import { RecentBookingsResponseDTO } from './dto/RecentBookingsResponseDTO';
import { SliceResponseRecentBookingsDTO } from './dto/SliceResponseRecentBookingsDTO';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {

  private httpClient = inject(HttpClient);
  private readonly API_URL = `${environment.gatewayUrl}`;

  getUserStats(): Observable<DashboardStatsResponseDTO> {
    return this.httpClient.get<DashboardStatsResponseDTO>(`${this.API_URL}/booking/dashboard/cardsData`);
  }
 
  getRecentBookings(page: number, size: number): Observable<SliceResponseRecentBookingsDTO<RecentBookingsResponseDTO>> {

    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.httpClient.get<SliceResponseRecentBookingsDTO<RecentBookingsResponseDTO>>(`${this.API_URL}/booking/dashboard/recentBookings`, { params });
  }

  getLatestTrip():Observable<any>{
    return this.httpClient.get(`${this.API_URL}/itinerary/trips/my-trips/latest`);
  }

  getLoggedInUserDetails():Observable<any>{
    return this.httpClient.get(`${this.API_URL}/auth/user/profile`);
  }
}



