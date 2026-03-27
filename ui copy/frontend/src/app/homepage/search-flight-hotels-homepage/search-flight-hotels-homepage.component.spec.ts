import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchFlightHotelsHomepageComponent } from './search-flight-hotels-homepage.component';

describe('SearchFlightHotelsHomepageComponent', () => {
  let component: SearchFlightHotelsHomepageComponent;
  let fixture: ComponentFixture<SearchFlightHotelsHomepageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchFlightHotelsHomepageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchFlightHotelsHomepageComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
