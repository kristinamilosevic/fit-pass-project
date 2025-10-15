import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FacilityService } from '../../services/facility/facility.service';
import { Facility } from '../../models/Facility';
import { Discipline } from '../../models/Discipline';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WorkDay } from '../../models/WorkDay';
import { DayOfWeek } from '../../models/DayOfWeek';

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
      next: (data) => {
        this.facility = this.convertToFacility(data);
      },
      error: (err) => console.error('Greška prilikom učitavanja objekta:', err)
    });
  }
  
  convertToFacility(data: any): Facility {
  
    const disciplines = data.disciplines || [];
    const workDays = data.workDays || [];
  
  
    return new Facility(
      data.id,
      data.name,
      data.description,
      new Date(data.createdAt),
      data.address,
      data.city,
      data.totalRating || 0,
      data.active || false,
      data.reviews || [],
      data.exercises || [],
      data.manages || [],
      data.images || [],
      disciplines.map((d: any) => new Discipline(d.id, d.name, data.id)), 
      workDays.map((wd: any) => new WorkDay(wd.id, new Date(wd.validFrom), wd.day as DayOfWeek, wd.fromTime, wd.untilTime, data.id)) 
    );
  }
  
  
  
  onSubmit(): void {
    if (this.facility) {
      this.facilityService.updateFacility(this.id, this.facility).subscribe({
        next: () => {
          this.router.navigate(['/facilities']); 
        },
        error: () => console.error('Error updating facility')
      });
    }
  }
}
