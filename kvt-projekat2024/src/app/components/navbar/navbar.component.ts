import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'nav-bar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  imports: [CommonModule]
})
export class NavBarComponent implements OnInit {
  userRole: string | null = null; // To store the user role

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.loadUserRole();
  }

  loadUserRole() {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.authService.getUserTypeByEmail(email).subscribe({
        next: (response) => {
          this.userRole = response.userType; 
        },
        error: (err) => {
          console.error('Error fetching user role:', err);
        }
      });
    }
  }

  isAdmin(): boolean {
    return this.userRole === 'ADMIN';
  }

  isManager(): boolean {
    return this.userRole === 'MANAGER';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']).then(() => {
      window.location.reload();
    });; 
  }

  createFacility() {
    this.router.navigate(['/add-facility']);
  }

}
