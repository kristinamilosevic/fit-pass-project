import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';


@Component({
  selector: 'login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, FormsModule]
})

export class LoginComponent {
  email: string = ''; 
  password: string = '';
  token: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.email && this.password) {
      this.authService.login(this.email, this.password).subscribe({
        next: (token: string) => {
          this.token = token;
          localStorage.setItem('authToken', token);
          localStorage.setItem('userEmail', this.email);

          this.authService.getUserTypeByEmail(this.email).subscribe({
            next: (response) => {
              localStorage.setItem('userRole', response.userType); 
              alert('Login successful');
              this.router.navigate(['/facilities']).then(() => {
                window.location.reload();
              });
            },
            error: (err) => {
              console.error('Error fetching user role:', err);
              alert('Login successful, but failed to fetch user role');
            }
          });
        },
        error: (err) => {
          console.error('Login failed:', err);
          alert('Invalid credentials');
        }
      });
    } else {
      alert('Please fill in both email and password');
    }
  }  
  
}
