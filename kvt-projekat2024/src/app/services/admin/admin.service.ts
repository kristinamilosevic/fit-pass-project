import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private baseUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  approveAccountRequest(id: number): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/approve/${id}`, {});
  }

  rejectAccountRequest(id: number, rejectionReason: string): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/reject/${id}`, { rejectionReason });
  }
}
