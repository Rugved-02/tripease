import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardPaymentMethodComponent } from './card-payment-method.component';

describe('CardPaymentMethodComponent', () => {
  let component: CardPaymentMethodComponent;
  let fixture: ComponentFixture<CardPaymentMethodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardPaymentMethodComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardPaymentMethodComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
