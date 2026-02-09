import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoTextComponent } from './logo-text.component';

describe('LogoTextComponent', () => {
  let component: LogoTextComponent;
  let fixture: ComponentFixture<LogoTextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogoTextComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoTextComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
