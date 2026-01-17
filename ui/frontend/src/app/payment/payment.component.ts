import { CurrencyPipe, NgIf, NgClass } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { MenubarComponent } from "../shared/components/menubar/menubar.component";
import { CardPaymentMethodComponent } from "./card-payment-method.component/card-payment-method.component";
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-payment',
  imports: [CardModule, CurrencyPipe, FormsModule, NgIf, CardPaymentMethodComponent],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css',
})
export class PaymentComponent implements OnInit {

  private route = inject(ActivatedRoute);

  bookingId:string = "";
  serviceFee:number = 0.00;
    taxes:number = 0.00;
  processingFee:number = 0.00;

  totalFee:number = 0.00;

  ngOnInit() {
  this.route.queryParams.subscribe(params => {
    this.bookingId = params['id'];
    this.serviceFee = params['price'];
    console.log(params['flightNo']);    
    console.log(params['price']); 
  });
  this.taxes = this.serviceFee * 0.10;
   this.processingFee = this.serviceFee * 0.03;

  this.totalFee = Number(this.serviceFee) +Number(this.taxes)+Number(this.processingFee);
  console.log(this.serviceFee +this.taxes+this.processingFee);
  console.log(this.totalFee);
}

  paymentMethod:PaymentMethod[] =[
    { icon:'pi pi-credit-card', methodInitial:'card', methodName:'Credit/Debit Card'},
    { icon:'pi pi-wallet', methodInitial:'wallet', methodName:'Digital Wallet'},
    { icon:'pi pi-building-columns', methodInitial:'bank', methodName:'Bank Transfer'},
  ];
  
  


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

