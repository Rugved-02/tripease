import { CurrencyPipe, NgIf, NgClass } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { MenubarComponent } from "../shared/components/menubar/menubar.component";
import { CardPaymentMethodComponent } from "./card-payment-method.component/card-payment-method.component";
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../core/services/payment/payment.service';
import { PaymentStateService } from '../core/services/payment/payment-state.service';
import { catchError, forkJoin, of, timer } from 'rxjs';

@Component({
  selector: 'app-payment',
  imports: [CardModule, CurrencyPipe, FormsModule, NgIf, CardPaymentMethodComponent],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css',
})
export class PaymentComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private paymentService = inject(PaymentService);
  private paymentStateService = inject(PaymentStateService);

isProcessing = signal<boolean>(false);
paymentStatus = signal<'IDLE' | 'SUCCESS' | 'FAILED'>('IDLE');
statusMessage = signal<string>('Processing your payment...');


  bookingId: string = '';
  totalAmount: number = 0;

  serviceFee:number = 0.00;
    taxes:number = 0.00;
  processingFee:number = 0.00;

  ngOnInit() {
    const data = this.paymentStateService.getSnapshot();

    if (data) {
      this.bookingId = data.bookingId;
  this.totalAmount = Number(data.amount); // Ensure it's a number
  
  // Calculate distributions based on the total
  // Using the total amount as the ceiling
  this.taxes = this.totalAmount * 0.10;
  this.processingFee = this.totalAmount * 0.03;
  
  // The service fee is whatever is left over
  this.serviceFee = this.totalAmount - (this.taxes + this.processingFee);

  console.log("Total Amount:", this.totalAmount);
  console.log("Breakdown:", {
    service: this.serviceFee,
    taxes: this.taxes,
    processing: this.processingFee
  });
    } else {
      // Security Check: If user refreshed or navigated directly to /payment 
      // without data, send them back to booking
      console.warn("No payment data found, redirecting...");
      this.router.navigate(['/dashboard']);
    }
}

  paymentMethod:PaymentMethod[] =[
    { icon:'pi pi-credit-card', methodInitial:'card', methodName:'Credit/Debit Card'},
    { icon:'pi pi-wallet', methodInitial:'wallet', methodName:'Digital Wallet'},
    { icon:'pi pi-building-columns', methodInitial:'bank', methodName:'Bank Transfer'},
  ];
  
  


  selectedMethod: string = 'card';

  selectMethod(method: string): void {
    this.selectedMethod = method;
  }
  

  executeCompletePayment(paymentDetails: any): void {
    console.log('Processing payment via:', this.selectedMethod);
    console.log('Payment Method Details:',paymentDetails);

    this.isProcessing.set(true);
    this.paymentStatus.set('IDLE');
    this.statusMessage.set('Processing your payment...');

    const payNow$ = this.paymentService.payNow(this.bookingId,this.selectedMethod).pipe(
    catchError(error => {
      console.error('Payment API failed', error);
      return of({ success: false }); // Return a "fail" object so the stream continues
    })
  );

  const visualTimer$ = timer(7000);

  forkJoin([payNow$,visualTimer$]).subscribe({
    next: ([payNowResponse]) => {

      console.log(payNowResponse);
      this.isProcessing.set(false); // Stop the spinner

      console.log("payNowResponse.status"+payNowResponse.status);
      
      if (payNowResponse && payNowResponse.status == "CONFIRMED") {
        this.paymentStatus.set('SUCCESS');
        this.statusMessage.set(payNowResponse.displayMessage);
      } else {
        this.paymentStatus.set('FAILED');
        this.statusMessage.set(payNowResponse.displayMessage);
      }
    },
    error: (err) => {
      console.log(err);
    this.isProcessing.set(false);
    this.paymentStatus.set('FAILED');
  }
  });


//     this.paymentService.payNow(this.bookingId,this.selectedMethod).subscribe({
//   next: (response) => {
//     console.log('Payment successful!', response);
//     // Redirect to success page or update UI
//   },
//   error: (err) => {
//     console.error('Payment failed', err);
//     // Show a toast message or alert to the user
//   }
// });
  }


  closePopup() {
    this.isProcessing.set(false);
    this.paymentStatus.set('IDLE');
    this.router.navigate(["/dashboard"]);
  }
}

export interface PaymentMethod {
  icon:string;
  methodInitial:string;
  methodName:string;
}

