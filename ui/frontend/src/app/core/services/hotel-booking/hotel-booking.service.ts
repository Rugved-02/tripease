import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';

export interface Hotel {
  id:string;
  name: string;
  location: string;
  image: string;
  price: number;
  rating: number;
  stars: number;
  reviews: number;
  roomsLeft: number;
}

@Injectable({
  providedIn: 'root',
})
export class HotelBookingService {

    private http = inject(HttpClient);
  private readonly API_URL = `${environment.gatewayUrl}/hotel`;

  /**
   * Fetches Top 15 hotels for the landing page.
   * Hits GET http://localhost:8083/hotel
   */
  getInitialHotels(): Observable<any[]> {
    return this.http.get<any[]>(this.API_URL);
  }

  /**
   * Fetches hotels based on location and dates.
   * Hits GET http://localhost:8083/hotel/search
   */
  searchHotels(location: string, checkIn: string, checkOut: string): Observable<any[]> {
    const params = new HttpParams()
      .set('location', location)
      .set('checkIn', checkIn)
      .set('checkOut', checkOut);
    
    return this.http.get<any[]>(`${this.API_URL}/search`, { params });
  }

  activateHotel(id: number): Observable<string> {
    return this.http.patch(`${this.API_URL}/${id}/activate`, {}, { responseType: 'text' });
  }

  deactivateHotel(id: number): Observable<string> {
    return this.http.patch(`${this.API_URL}/${id}/deactivate`, {}, { responseType: 'text' });
  }
}
