import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import { v4 as uuidv4 } from 'uuid';
import { PaymentRequestDTO } from './dto/PaymentRequestDTO';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { PaymentStateService } from './payment-state.service';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.gatewayUrl}/payment`;

  private router = inject(Router);
  private paymentStateService = inject(PaymentStateService);

  private currentIdempotencyKey: string = "";
  private currentBookingId: string | null = null;
  
  

  getIdempotencyKey(bookingId: string): string {
    // If the booking ID hasn't changed, return the existing key
    if (this.currentBookingId === bookingId && this.currentIdempotencyKey) {
      return this.currentIdempotencyKey;
    }
    
    // Otherwise, generate a new one (new booking or fresh attempt)
    this.currentIdempotencyKey = uuidv4();
    this.currentBookingId = bookingId;
    return this.currentIdempotencyKey;
  }

  // Use this if the user manually changes payment method (e.g., Credit Card to UPI)
  resetIdempotencyKey() {
    this.currentIdempotencyKey = uuidv4();
  }

  processPayment(data: PaymentRequestDTO): Observable<any>{
    return this.http.post(`${this.API_URL}/process`, data);
  }

  payNow(bookingId: string, selectedMethod: string): Observable<any> {
  // Get or generate the key
  const key = this.getIdempotencyKey(bookingId);
  console.log("payNow(): Idempotency key" +key)

  // Set the header to match the @RequestHeader name in Spring Boot
  const headers = new HttpHeaders({
    'Idempotency-Key': key
  });

  // Note: Since your controller doesn't show a @RequestBody, 
  // we pass an empty object or the method as a param
  return this.http.post(
    `${this.API_URL}/payNow`, 
    {}, 
    { headers }
  );
}


  preparePaymentEntry(bookingId: string, amount: number) {
  // Generate the key once for this booking session
  const key = this.getIdempotencyKey(bookingId);
  
  console.log("preparePaymentEntry () called"+bookingId,amount,key);

  const paymentRequestDTO: PaymentRequestDTO = {
    idempotencyKey: key,
    bookingId: bookingId,
    amount: amount
  };

  // Hit the "process payment" endpoint to create the PENDING entry
  this.processPayment(paymentRequestDTO).subscribe({
    next: (res) => {
      this.paymentStateService.setPaymentInfo({
      bookingId: paymentRequestDTO.bookingId,
      amount: paymentRequestDTO.amount
    });
      this.router.navigate(['/payment']); // Move to the UI where they pick a method
    },
    error: (err) => console.error("Could not initialize payment", err)
  });
}
  
}
