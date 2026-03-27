import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class hotelapiservice {
  // Corrected to match @RequestMapping("/hotel")
  private baseUrl = 'http://localhost:8083/hotel'; 

  constructor(private http: HttpClient) {}

  /**
   * Fetches Top 15 hotels for the landing page.
   * Hits GET http://localhost:8083/hotel
   */
  getInitialHotels(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
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
    
    return this.http.get<any[]>(`${this.baseUrl}/search`, { params });
  }

  activateHotel(id: number): Observable<string> {
    return this.http.patch(`${this.baseUrl}/${id}/activate`, {}, { responseType: 'text' });
  }

  deactivateHotel(id: number): Observable<string> {
    return this.http.patch(`${this.baseUrl}/${id}/deactivate`, {}, { responseType: 'text' });
  }
}