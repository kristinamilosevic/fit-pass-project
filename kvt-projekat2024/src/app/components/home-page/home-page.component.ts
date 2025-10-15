import { Component, OnInit } from '@angular/core';
import { FacilityService } from '../../services/facility/facility.service';
import { UserService } from '../../services/user/user.service';
import { Facility } from '../../models/Facility'; 
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-home-page',
  standalone: true,
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
  imports: [CommonModule, FormsModule, RouterModule],
})
export class HomePageComponent implements OnInit {
  facilities: Facility[] = [];
  topRatedFacilities: Facility[] = [];
  userFacilities: Facility[] = [];
  unvisitedFacilities: Facility[] = [];  
  email!: string;

  constructor(
    private userService: UserService, 
    private facilityService: FacilityService
  ) {}

  ngOnInit(): void {
    this.loadFacilities();
    this.loadTopRatedFacilities();
    this.loadUserFacilities();
  }

  loadFacilities(): void {
    this.facilityService.getFacilitiesForLoggedInUser().subscribe(
      (data) => {
        this.facilities = data;
      },
      (error) => {
        console.error('Greška pri dobavljanju facility-ja:', error);
      }
    );
  }

  loadTopRatedFacilities(): void {
    this.facilityService.getTopRatedFacilities().subscribe(
      (data) => {
        this.topRatedFacilities = data;
      },
      (error) => {
        console.error('Greška pri dobavljanju top ocenjenih objekata:', error);
      }
    );
  }

  loadUserFacilities(): void {
    const token = localStorage.getItem('authToken'); 

    if (token) {
      try {
        const decodedToken: any = jwt_decode(token);
        this.email = decodedToken.sub;

        console.log('Decoded email:', this.email);

        if (this.email) {
          console.log('Calling getUserIdByEmail with email:', this.email);
          this.userService.getUserIdByEmail(this.email).subscribe(
            (userId) => {
              
              this.loadUnvisitedFacilities(userId);
              this.facilityService.getFacilitiesByUserId(userId).subscribe(
                (data) => {
                  this.userFacilities = data;
                },
                (error) => {
                  console.error('Greška pri dobavljanju objekata za korisnika:', error);
                }
              );
              
            },
            (error) => {
              console.error('Greška pri dobavljanju ID-a korisnika:', error);
            }
          );
        }
      } catch (error) {
        console.error('Greška pri dekodiranju tokena:', error);
      }
    } else {
      console.error('Token nije pronađen u localStorage.');
    }
  }

  loadUnvisitedFacilities(userId: number): void {
    this.facilityService.getUnvisitedFacilitiesByUserId(userId).subscribe(
      (data) => {
        this.unvisitedFacilities = data; 
        console.log('Neposećene teretane:', this.unvisitedFacilities);
      },
      (error) => {
        console.error('Greška pri dobavljanju neposećenih teretana:', error);
      }
    );
  }
}
