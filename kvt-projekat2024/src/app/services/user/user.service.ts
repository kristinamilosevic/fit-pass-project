import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../../models/User";
import { catchError, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users'; 
  private updateUrl = 'http://localhost:8080/api/users/update';
  private countExerciseUrl = 'http://localhost:8080/api/exercises';

  constructor(private http: HttpClient) {}

  getUserIdByEmail(email: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/idByEmail?email=${email}`);
  }

  getUserByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/by-email?email=${email}`);
  }

  updateUser(email: string, user: User): Observable<string> {
    return this.http.put<string>(`${this.updateUrl}?email=${email}`, user, {
      responseType: 'text' as 'json'
    });
  }

  getVisitedFacilitiesWithCounts(userEmail: string): Observable<any> {
    return this.http.get(`${this.countExerciseUrl}/visited-facilities/${userEmail}`)
      .pipe(
        tap(response => {
          console.log('Received response:', response);
        }),
        catchError(error => {
          console.error('Request failed with error:', error);
          throw error; 
        })
      );
  }
  
}
