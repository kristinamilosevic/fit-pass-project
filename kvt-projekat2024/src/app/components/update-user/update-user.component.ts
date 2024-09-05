import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { ReviewService } from '../../services/review/review.service';
import { User } from '../../models/User';
import { Review } from '../../models/Review';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-user-update',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {
  user: User = new User(0, '', '', '', '', new Date(), '', new Date(), '', '', '', [], undefined, [], [], '');
  email: string = '';
  message: string = '';
  reviews: Review[] = []; 
  visitedFacilities: any[] = [];

  constructor(
    private userService: UserService, 
    private reviewService: ReviewService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.email = localStorage.getItem('userEmail') || ''; 

    if (this.email) {
      this.userService.getUserByEmail(this.email).subscribe({
        next: (data: User) => {
          this.user = data;
          this.loadUserReviews(); 
          this.loadVisitedFacilities(); 
        },
        error: (error) => {
          this.message = 'Error fetching user data: ' + error.message;
        }
      });
    } else {
      this.message = 'Email not found in localStorage.';
    }
  }

  navigateToChangePassword() {
    this.router.navigate(['/change-password']);
  }

  loadUserReviews() {
    this.reviewService.getReviewsByUserId(this.user.id).subscribe({
      next: (reviews: Review[]) => {
        this.reviews = reviews;
      },
      error: (error) => {
        this.message = 'Error fetching user reviews: ' + error.message;
      }
    });
  }
  
  loadVisitedFacilities() {
    this.userService.getVisitedFacilitiesWithCounts(this.email).subscribe({
      next: (facilities: any[]) => {
        this.visitedFacilities = facilities;
      },
      error: (error) => {
        this.message = 'Error fetching visited facilities: ' + error.message;
      }
    });
  }

  onSubmit() {
    this.userService.updateUser(this.email, this.user).subscribe({
      next: () => {
        this.message = 'User successfully updated!';
        alert('User successfully updated!');
        this.router.navigate(['/update-user']); // Redirekcija nakon uspešne izrade
      },
      error: (error) => {
        this.message = 'Error updating user: ' + error.message;
        alert('Error updating user: ' + error.message);
      }
    });
  }
}
