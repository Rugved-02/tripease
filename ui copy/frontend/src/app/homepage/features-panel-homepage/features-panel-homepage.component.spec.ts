import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeaturesPanelHomepageComponent } from './features-panel-homepage.component';

describe('FeaturesPanelHomepageComponent', () => {
  let component: FeaturesPanelHomepageComponent;
  let fixture: ComponentFixture<FeaturesPanelHomepageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeaturesPanelHomepageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeaturesPanelHomepageComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
