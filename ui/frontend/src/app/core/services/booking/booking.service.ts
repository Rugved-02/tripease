import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import { BookingRequestDTO } from './dto/BookingRequestDTO';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BookingService {

  private http = inject(HttpClient);
  private readonly API_URL = `${environment.gatewayUrl}/booking`;

  createBooking(data: BookingRequestDTO): Observable<any> {
    return this.http.post(`${this.API_URL}`, data);
  }
  
}
