// src/app/services/facility.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Facility } from '../../models/Facility';
import { WorkDay } from '../../models/WorkDay';

@Injectable({
  providedIn: 'root'
})
export class FacilityService {
  private apiUrl = 'http://localhost:8080/api/facilities';
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getAllFacilities(): Observable<Facility[]> {
    return this.http.get<Facility[]>(this.apiUrl);
  }

  getFacilityById(id: number): Observable<Facility> {
    return this.http.get<Facility>(`${this.apiUrl}/${id}`);
  }

  createFacility(facility: Facility): Observable<any> {
    return this.http.post<any>(this.apiUrl, facility);
  }

  updateFacility(id: number, facility: Facility): Observable<Facility> {
    return this.http.put<Facility>(`${this.apiUrl}/${id}`, facility);
  }

  deleteFacility(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getWorkDaysByFacilityId(facilityId: number): Observable<WorkDay[]> {
    return this.http.get<WorkDay[]>(`${this.apiUrl}/${facilityId}/workdays`);
  }

  countExercisesByUserIdAndFacilityId(userId: number, facilityId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/exercises/count/${userId}/${facilityId}`);
  }
}