import { Component, OnInit } from '@angular/core';
import { FacilityService } from '../../services/facility/facility.service'; 
import { Facility } from '../../models/Facility';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-inactive-facilities',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './inactive-facilities.component.html',
  styleUrls: ['./inactive-facilities.component.css']
})

export class InactiveFacilitiesComponent implements OnInit {
  inactiveFacilities: any[] = [];
  users: any[] = []; 
  selectedUser: { [facilityId: number]: number } = {};
  errorMessage: string | null = null;

  constructor(private facilityService: FacilityService, private router: Router, private http: HttpClient) {}

  ngOnInit() {
    this.facilityService.getInactiveFacilities().subscribe(
      data => {
        this.inactiveFacilities = data;
      },
      error => {
        this.errorMessage = 'Error loading facilities';
      }
    );

    this.facilityService.getUsers().subscribe(
      data => {
        this.users = data;
      },
      error => {
        this.errorMessage = 'Error loading users';
      }
    );
  }

  assignManager(facilityId: number) {
    const userId = this.selectedUser[facilityId];
    const startDate = new Date();
    const endDate = new Date();
    endDate.setMonth(startDate.getMonth() + 1);
  
    if (userId) {
      const manageData = {
        userId: userId,
        facilityId: facilityId,
        startDate: new Date().toISOString().split('T')[0],
        endDate: endDate.toISOString().split('T')[0]
      };
  
      this.http.post('http://localhost:8080/api/manages', manageData, { observe: 'response' })
        .subscribe({
          next: (response) => {
            if (response.status === 201) {
              alert('Manager assigned and facility updated successfully.');
              this.router.navigate(['/inactive-facilities']).then(() => {
                window.location.reload();
              });
            }
          },
          error: (err) => {
            if (err.status === 400) {
              alert('Invalid user or facility ID.');
            } else {
              alert('Manager assigned and facility updated successfully.');
              this.router.navigate(['/inactive-facilities']).then(() => {
                window.location.reload();
              });
            }
          }
        });
    } else {
      alert('Please select a user.');
    }
  }
}  
