import { Component } from '@angular/core';
import { FacilityService } from '../../services/facility/facility.service';
import { Facility } from '../../models/Facility';
import { WorkDay } from '../../models/WorkDay';
import { Discipline } from '../../models/Discipline';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DayOfWeek } from '../../models/DayOfWeek';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-facility',
  standalone: true,
  templateUrl: './add-facility.component.html',
  styleUrls: ['./add-facility.component.css'],
  imports: [CommonModule, FormsModule]
})
export class AddFacilityComponent {
  facility: Facility = new Facility(
    0,
    '',
    '',
    new Date(),
    '',
    '',
    0,
    true,
    [],
    [],
    [],
    [],
    [],
    []
  );

  daysOfWeek = Object.values(DayOfWeek);

  constructor(private facilityService: FacilityService, private router: Router) { }

  onSubmit() {
    const facilityToSend = {
      ...this.facility,
      createdAt: this.facility.createdAt, // Ostavite kao Date objekat
      workDays: this.facility.workDays.map(workDay => ({
        ...workDay,
        validFrom: workDay.validFrom, // Ostavite kao Date objekat
        facility: undefined // Uklonite referencu na ceo objekat
      })),
      disciplines: this.facility.disciplines.map(discipline => ({
        ...discipline,
        facility: undefined // Uklonite referencu na ceo objekat
      }))
    };
  
    this.facilityService.createFacility(facilityToSend).subscribe(response => {
      console.log('Facility added', response);
      this.router.navigate(['/facilities']);
    });
  }
  
  

  addWorkDay() {
    const workDay: WorkDay = {
      id: 0,
      validFrom: new Date(),
      day: this.daysOfWeek[0],
      fromTime: '',
      untilTime: '',
      facility: this.facility // Koristimo objekat facility
    };
    this.facility.workDays.push(workDay);
  }

  removeWorkDay(index: number) {
    this.facility.workDays.splice(index, 1);
  }

  addDiscipline() {
    const discipline: Discipline = {
      id: 0,
      name: '',
      facility: this.facility // Koristimo objekat facility
    };
    this.facility.disciplines.push(discipline);
  }

  removeDiscipline(index: number) {
    this.facility.disciplines.splice(index, 1);
  }
}
