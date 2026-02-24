import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RatingAndReviewsComponent } from './rating-and-reviews.component';

describe('RatingAndReviewsComponent', () => {
  let component: RatingAndReviewsComponent;
  let fixture: ComponentFixture<RatingAndReviewsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RatingAndReviewsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RatingAndReviewsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
