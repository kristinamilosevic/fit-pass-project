import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Facility } from '../../models/Facility';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-facility-list',
  standalone: true,
  templateUrl: './facility-list.component.html',
  styleUrls: ['./facility-list.component.css'],
  imports: [CommonModule, RouterModule, FormsModule]
})
export class FacilityListComponent implements OnInit {

  facilities: Facility[] = [];
  searchCriteria: any = {
    city: '',
    discipline: '',
    minRating: null,
    maxRating: null,
    hasWorkDays: false
  };
  userType: string | null = null; // To store user type

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.getAllFacilities();
    this.userType = localStorage.getItem('userRole'); // Retrieve user type from localStorage
  }

  getAllFacilities() {
    this.http.get<Facility[]>('http://localhost:8080/api/facilities')
      .subscribe(data => {
        this.facilities = data;
      });
  }

  searchFacilities() {
    const params = new URLSearchParams();

    if (this.searchCriteria.city) {
      params.append('city', this.searchCriteria.city);
    }
    if (this.searchCriteria.discipline) {
      params.append('discipline', this.searchCriteria.discipline);
    }
    if (this.searchCriteria.minRating !== null) {
      params.append('minRating', this.searchCriteria.minRating.toString());
    }
    if (this.searchCriteria.maxRating !== null) {
      params.append('maxRating', this.searchCriteria.maxRating.toString());
    }
    if (this.searchCriteria.hasWorkDays) {
      params.append('hasWorkDays', 'true');
    }

    this.http.get<Facility[]>(`http://localhost:8080/api/facilities/search?${params.toString()}`)
      .subscribe(data => {
        this.facilities = data;
      });
  }

  resetSearch() {
    this.searchCriteria = {
      city: '',
      discipline: '',
      minRating: null,
      maxRating: null,
      hasWorkDays: false
    };
    this.getAllFacilities();
  }

  deleteFacility(id: number) {
    this.http.delete(`http://localhost:8080/api/facilities/${id}`)
      .subscribe(() => {
        console.log('Facility deleted');
        this.getAllFacilities();  
      });
  }

  editFacility(id: number) {
    this.router.navigate(['/facilities/update', id]);
  }

}