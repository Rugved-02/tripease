import { CurrencyPipe, NgIf, NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { MenubarComponent } from "../shared/components/menubar/menubar.component";
import { CardPaymentMethodComponent } from "./card-payment-method.component/card-payment-method.component";

@Component({
  selector: 'app-payment',
  imports: [CardModule, CurrencyPipe, FormsModule, NgIf, MenubarComponent, NgClass, CardPaymentMethodComponent],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css',
})
export class PaymentComponent {

  paymentMethod:PaymentMethod[] =[
    { icon:'pi pi-credit-card', methodInitial:'card', methodName:'Credit/Debit Card'},
    { icon:'pi pi-wallet', methodInitial:'wallet', methodName:'Digital Wallet'},
    { icon:'pi pi-building-columns', methodInitial:'bank', methodName:'Bank Transfer'},
  ];
  
  bookingId:string = "FL001";
  serviceFee:number = 350.00;
  taxes:number = 52.50;
  processingFee:number = 7.50;

  totalFee:number = this.serviceFee+this.taxes+this.processingFee;

  selectedMethod: string = 'card';

  // Card Form Data (Placeholder for Reactive Forms)
  cardDetails = {
    name: '',
    number: '',
    expiry: '',
    cvv: ''
  };

  selectMethod(method: string): void {
    this.selectedMethod = method;
  }

  completePayment(): void {
    console.log('Processing payment via:', this.selectedMethod);
    // Add payment gateway logic here
  }
}

export interface PaymentMethod {
  icon:string;
  methodInitial:string;
  methodName:string;
}
