import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityComponent } from './facility.component';

describe('FacilityComponent', () => {
  let component: FacilityComponent;
  let fixture: ComponentFixture<FacilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FacilityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
