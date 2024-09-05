import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FacilityService } from '../../services/facility/facility.service';
import { ExerciseService } from '../../services/exercise/exercise.service';
import { UserService } from '../../services/user/user.service';
import { Facility } from '../../models/Facility';
import { WorkDay } from '../../models/WorkDay';
import { Review } from '../../models/Review';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-facility',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './facility.component.html',
  styleUrls: ['./facility.component.css']
})
export class FacilityComponent implements OnInit {
  facility: Facility | null = null;
  workDays: WorkDay[] = [];
  canReview: boolean = false;
  facilityId: number = 0;
  userId: number | null = null;
  email: string | null = null;
  reviews: Review[] = [];
  userType: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router, 
    private facilityService: FacilityService,
    private exerciseService: ExerciseService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userType = localStorage.getItem('userRole');
    this.facilityId = +this.route.snapshot.paramMap.get('id')!;
    console.log('Facility ID:', this.facilityId);

    const token = localStorage.getItem('authToken');

    if (token) {
      const decodedToken: any = jwt_decode(token);
      this.email = decodedToken.sub; // Pretpostavljam da je email u 'sub' polju
      console.log('Decoded email:', this.email);
      if (this.email) {
        console.log('Calling getUserIdByEmail with email:', this.email); 
        this.getUserIdByEmail(this.email);
      }
    }

    this.loadFacility();
    this.loadReviews();
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

  loadReviews(): void { 
    this.facilityService.getReviewsByFacilityId(this.facilityId).subscribe({
      next: (data) => {
        this.reviews = data;
        console.log('Reviews loaded:', this.reviews);
      },
      error: (err) => {
        console.error('Error loading reviews:', err);
        alert(`Error loading reviews: ${err.message}`);
      }
    });
  }

  loadWorkDays(facilityId: number): void {
    this.facilityService.getWorkDaysByFacilityId(facilityId).subscribe({
      next: (data) => {
        this.workDays = data;
        console.log('Work days loaded:', this.workDays);
      },
      error: (err) => {
        console.error('Error loading work days:', err);
        alert(`Error loading work days: ${err.message}`);
      }
    });
  }

  getUserIdByEmail(email: string): void {
    this.userService.getUserIdByEmail(email).subscribe({
      next: (id) => {
        this.userId = id; 
        console.log('User ID fetched:', this.userId);
        if (this.userId !== null) {
          this.checkIfCanReview();
        }
      },
      error: (err) => {
        console.error('Error fetching user ID:', err);
        alert('Error fetching user ID');
      }
    });
  }

  checkIfCanReview(): void {
    if (this.userId === null || this.facilityId === 0) {
      console.error('User ID or Facility ID is not available');
      return;
    }

    this.facilityService.countExercisesByUserIdAndFacilityId(this.userId, this.facilityId).subscribe({
      next: (count) => {
        this.canReview = count > 0; 
        console.log('Number of exercises:', count);
      },
      error: (err) => {
        console.error('Error counting exercises:', err);
        alert('Error counting exercises');
      }
    });
  }

  scheduleVisit(facilityId: number): void {
    this.router.navigate(['/exercise', facilityId]);  
  }

  review(facilityId: number): void {
    this.router.navigate(['/review', facilityId]);  
  }

  hideReview(reviewId: number): void {
    this.facilityService.hideReview(reviewId).subscribe({
      next: () => {
        alert('Review successfully hidden.');
        this.loadReviews();
      },
      error: (err) => {
        console.error('Error hiding review:', err);
        alert('Error hiding review.');
      }
    });
  }

  deleteReview(reviewId: number): void {
    this.facilityService.deleteReview(reviewId).subscribe({
      next: response => {
        alert('Review deleted successfully.');
        this.loadReviews();
      },
      error: (err) => {
        console.error('Error deleting review:', err);
        alert('Error deleting review.');
      }
    });
  }
}
