import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class hotelapiservice {
  private baseUrl = 'http://localhost:8083/hotels';

  constructor(private http: HttpClient) {}

  // Fetch all or search by location
  getHotels(location?: string): Observable<any[]> {
    let params = new HttpParams();
    if (location) params = params.append('location', location);
    // Returns the observable immediately to the component
    return this.http.get<any[]>(this.baseUrl, { params });
  }

  activateHotel(id: number): Observable<string> {
    return this.http.patch(`${this.baseUrl}/${id}/activate`, {}, { responseType: 'text' });
  }

  deactivateHotel(id: number): Observable<string> {
    return this.http.patch(`${this.baseUrl}/${id}/deactivate`, {}, { responseType: 'text' });
  }
}