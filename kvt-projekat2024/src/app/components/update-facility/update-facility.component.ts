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
        console.log('Odgovor sa servera:', data); // Logovanje odgovora sa servera
        this.facility = this.convertToFacility(data);
        console.log('Disciplines data:', data.disciplines);
      console.log('WorkDays data:', data.workDays);
        console.log('Facility nakon konverzije:', this.facility); // Logovanje objekta nakon konverzije
      },
      error: (err) => console.error('Greška prilikom učitavanja objekta:', err)
    });
  }
  
  convertToFacility(data: any): Facility {
    console.log('Raw data for conversion:', data);
  
    // Proveri da li `disciplines` i `workDays` postoje u odgovoru
    const disciplines = data.disciplines || [];
    const workDays = data.workDays || [];
  
    // Loguj podatke za debugging
    console.log('Disciplines data:', disciplines);
    console.log('WorkDays data:', workDays);
  
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
      disciplines.map((d: any) => new Discipline(d.id, d.name, data.id)), // Pretpostavi da je `data.id` ID facility-a
      workDays.map((wd: any) => new WorkDay(wd.id, new Date(wd.validFrom), wd.day as DayOfWeek, wd.fromTime, wd.untilTime, data.id)) // Pretpostavi da je `data.id` ID facility-a
    );
  }
  
  
  
  onSubmit(): void {
    if (this.facility) {
      this.facilityService.updateFacility(this.id, this.facility).subscribe({
        next: () => {
          console.log('Facility updated');
          this.router.navigate(['/facilities']); // Preusmeravanje nakon ažuriranja
        },
        error: () => console.error('Error updating facility')
      });
    }
  }
}
