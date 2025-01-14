import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReviewService } from '../../services/review/review.service';
import { UserService } from '../../services/user/user.service';
import { ExerciseService } from '../../services/exercise/exercise.service'; // Importujte ExerciseService
import jwt_decode from 'jwt-decode';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {
  commentText: string = '';
  exerciseCount: number = 0; 
  hidden: boolean = true; 
  isActive: boolean = true;
  rate = {
    equipment: 0,
    staff: 0,
    hygiene: 0,
    space: 0
  };
  userId: number | null = null;
  facilityId: number = 0;
  createdAt: string = '';
  commentInputVisible: boolean = false; 
  ratingError: boolean = false; 

  constructor(
    private reviewService: ReviewService,
    private userService: UserService,
    private exerciseService: ExerciseService, 
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.facilityId = +this.route.snapshot.paramMap.get('id')!;
    this.createdAt = new Date().toISOString(); 

    const token = localStorage.getItem('authToken');

    if (token) {
      const decodedToken: any = jwt_decode(token);
      const email = decodedToken.sub;
      if (email) {
        this.getUserIdByEmail(email);
      }
    }
  }
  
  getUserIdByEmail(email: string): void {
    this.userService.getUserIdByEmail(email).subscribe({
      next: (id) => {
        this.userId = id;
        if (this.userId) {
          this.getExerciseCount(this.userId);
        }
      },
      error: (err) => {
        console.error('Error fetching user ID:', err);
        alert('Error fetching user ID');
      }
    });
  }

  getExerciseCount(userId: number): void {
    this.exerciseService.countExercisesByUserId(userId).subscribe({
      next: (count) => {
        this.exerciseCount = count;
      },
      error: (err) => {
        console.error('Error fetching exercise count:', err);
        alert('Error fetching exercise count');
      }
    });
  }

  toggleCommentInput(): void {
    this.commentInputVisible = !this.commentInputVisible; 
  }

  checkRating(): void {
    
    this.ratingError = 
      this.rate.equipment > 10 ||
      this.rate.staff > 10 ||
      this.rate.hygiene > 10 ||
      this.rate.space > 10;
  }

  onSubmit(): void {
    if (!this.userId || !this.facilityId) {
      console.error('User ID or Facility ID is not available');
      return;
    }

    if (this.ratingError) {
      alert('Rating cannot exceed 10');
      return;
    }

    const reviewData = {
      commentText: this.commentText,
      exerciseCount: this.exerciseCount,
      hidden: this.hidden, 
      isActive: this.isActive,
      rate: this.rate,
      userId: this.userId,
      facilityId: this.facilityId,
      createdAt: this.createdAt
    };

    this.reviewService.createReview(reviewData).subscribe(
      response => {
        console.log('Review created successfully', response);
        alert('Review successfully submitted!');
        this.router.navigate(['/facilities']);
      },
      error => {
        console.error('Error creating review', error);
        alert('Error creating review');
      }
    );
}

}
