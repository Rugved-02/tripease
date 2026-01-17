import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItineraryPlanningComponent } from './itinerary-planning.component';

describe('ItineraryPlanningComponent', () => {
  let component: ItineraryPlanningComponent;
  let fixture: ComponentFixture<ItineraryPlanningComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItineraryPlanningComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItineraryPlanningComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
