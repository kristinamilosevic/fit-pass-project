import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InactiveFacilitiesComponent } from './inactive-facilities.component';

describe('InactiveFacilitiesComponent', () => {
  let component: InactiveFacilitiesComponent;
  let fixture: ComponentFixture<InactiveFacilitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InactiveFacilitiesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InactiveFacilitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
