import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FacilityService } from '../../services/facility/facility.service';
import { Facility } from '../../models/Facility';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-facility',
  standalone: true,
  templateUrl: './update-facility.component.html',
  styleUrls: ['./update-facility.component.css'],
  imports: [CommonModule, FormsModule]
})
export class UpdateFacilityComponent implements OnInit {
  facility: Facility | null = null;
  id!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private facilityService: FacilityService
  ) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.loadFacility();
  }

  loadFacility(): void {
    this.facilityService.getFacilityById(this.id).subscribe({
      next: (data) => this.facility = data,
      error: () => console.error('Error loading facility')
    });
  }

  onSubmit(): void {
    if (this.facility) {
      this.facilityService.updateFacility(this.id, this.facility).subscribe({
        next: () => {
          console.log('Facility updated');
          this.router.navigate(['/facilities']); // Redirect after update
        },
        error: () => console.error('Error updating facility')
      });
    }
  }
}
