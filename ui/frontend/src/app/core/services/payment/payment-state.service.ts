import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface PaymentInfo {
  bookingId: string;
  amount: number;
}

@Injectable({ providedIn: 'root' })
export class PaymentStateService {
  // Initialize with null
  private paymentData = new BehaviorSubject<PaymentInfo | null>(null);
  
  // Observable for components to subscribe to
  paymentData$ = this.paymentData.asObservable();

  // Call this BEFORE router.navigate
  setPaymentInfo(info: PaymentInfo) {
    this.paymentData.next(info);
  }

  // Helper to get data without subscribing (useful for one-time logic)
  getSnapshot(): PaymentInfo | null {
    return this.paymentData.value;
  }
}