import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FacilityService } from '../../services/facility/facility.service';
import { Facility } from '../../models/Facility'; 
import { ExerciseService } from '../../services/exercise/exercise.service';
import { UserService } from '../../services/user/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import jwt_decode from 'jwt-decode'; 
@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-exercise',
  templateUrl: './exercise.component.html',
  styleUrls: ['./exercise.component.css']
})
export class ExerciseComponent implements OnInit {
  facilityId: number = 0;
  facility: Facility | null = null;
  userId: number | null = null;
  email: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private facilityService: FacilityService,
    private exerciseService: ExerciseService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.facilityId = +this.route.snapshot.paramMap.get('id')!;
    console.log('Facility ID:', this.facilityId);
  
    const token = localStorage.getItem('authToken');
  
    if (token) {
      const decodedToken: any = jwt_decode(token);
      this.email = decodedToken.sub; 
      console.log('Decoded email:', this.email);
      if (this.email) {
        console.log('Calling getUserIdByEmail with email:', this.email); 
        this.getUserIdByEmail(this.email);
      }
    }
  
    this.loadFacility();
  }

  loadFacility(): void {
    this.facilityService.getFacilityById(this.facilityId).subscribe({
      next: (data) => this.facility = data,
      error: (err) => {
        console.error('Error loading facility:', err);
        alert('Error loading facility');
      }
    });
  }

  getUserIdByEmail(email: string): void {
    this.userService.getUserIdByEmail(email).subscribe({
        next: (id) => {
            this.userId = id; 
            console.log('User ID fetched:', this.userId);
        },
        error: (err) => {
            console.error('Error fetching user ID:', err);
            alert('Error fetching user ID');
        }
    });
  }

  submitSchedule(formValues: any) {
    if (!this.facilityId || this.userId === null) {
      console.error('Facility ID or User ID is not available');
      return;
    }

    const startDateTime = new Date(`${formValues.startDate}T${formValues.startTime}`);
    const endDateTime = new Date(`${formValues.startDate}T${formValues.endTime}`);

    const reservationData = {
      facilityId: this.facilityId,
      userId: this.userId,
      startTime: startDateTime.toISOString(),
      endTime: endDateTime.toISOString()
    };

    console.log('Schedule submitted for facility ID:', this.facilityId);
    console.log('Reservation data:', reservationData);

    this.exerciseService.reserveExercise(reservationData).subscribe(
      response => {
        console.log('Response from backend:', response);
        alert('Reservation successfully submitted!');
      },
      error => {
        console.error('Error occurred while sending data:', error);
        alert('Facility is closed during the selected time.');
      }
    );
  }
}
