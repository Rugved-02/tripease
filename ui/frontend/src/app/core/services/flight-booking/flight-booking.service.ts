import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';


@Injectable({
  providedIn: 'root',
})
export class FlightBookingService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.gatewayUrl}/flight`;


  getFlights(from: string, to: string, date: string, passengers: number): Observable<any[]> {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to)
      .set('date', date) // Matches @RequestParam("date")
      .set('passengers', passengers.toString());

    return this.http.get<any[]>(`${this.API_URL}/search`, { 
      params
    });
  }

  /**
   * Modified reserveSeats to fix the 'this.headers' error
   */
  reserveSeats(id: number, classType: string, date: string, qty: number): Observable<string> {
    const params = new HttpParams()
      .set('id', id.toString()) // Ensure number is converted to string for HttpParams
      .set('classType', classType.toUpperCase())
      .set('date', date)
      .set('qty', qty.toString());

    return this.http.put(`${this.API_URL}/inventory/reserve`, null, { 
      params,
      responseType: 'text' 
    });
  }
}