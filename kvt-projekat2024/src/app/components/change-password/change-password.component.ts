import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule]
})
export class ChangePasswordComponent {
  currentPassword: string = '';
  newPassword: string = '';
  confirmNewPassword: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit() {
    const token = localStorage.getItem('authToken');

    if (!token) {
      this.errorMessage = 'Nema validnog tokena.';
      return;
    }

    try {
      const decodedToken: any = jwt_decode(token);
      const email = decodedToken.sub; 

      const url = `http://localhost:8080/api/users/change-password?email=${email}`;

      const body = {
        currentPassword: this.currentPassword,
        newPassword: this.newPassword,
        confirmNewPassword: this.confirmNewPassword
      };

      this.http.put(url, body, { responseType: 'text' }).subscribe(
        (response: string) => {
          this.successMessage = response; 
          this.errorMessage = '';
          alert('Password has been successfully changed!');
          this.router.navigate(['/update-user']);
        },
        (error) => {
          this.errorMessage = error.error;
          this.successMessage = '';
        }
      );      

    } catch (error) {
      this.errorMessage = 'Greška u dekodiranju tokena.';
    }
  }
}
