import { Component, EventEmitter, inject, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormControl } from '@angular/forms';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-card-payment-method',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './card-payment-method.component.html',
  styleUrl: './card-payment-method.component.css',
})
export class CardPaymentMethodComponent implements OnInit {

  @Output() pay = new EventEmitter<any>;

  paymentForm: FormGroup = new FormGroup({
    cardName: new FormControl('', [Validators.required, Validators.minLength(3)]),
    cardNumber: new FormControl('', [
      Validators.required, 
      Validators.pattern('^[0-9 ]{16,19}$')
    ]),
    expiryDate: new FormControl('', [
      Validators.required, 
      Validators.pattern('^(0[1-9]|1[0-2])\/?([0-9]{2})$')
    ]),
    cvv: new FormControl('', [
      Validators.required, 
      Validators.pattern('^[0-9]{3,4}$')
    ])
  });

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.paymentForm.valid) {

      console.log("Card Payment Component:: inside onSubmit() with valid form");

      this.completePayment();
      console.log('Success:', this.paymentForm.value);
    } else {
      this.paymentForm.markAllAsTouched();
    }
  }

  completePayment(){
    console.log("Card Payment Component:: inside completePayment()");

    this.pay.emit({ method: 'CARD', details: this.paymentForm.value });
  }
}

