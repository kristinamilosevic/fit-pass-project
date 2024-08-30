import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {
  private apiUrl = 'http://localhost:8080/api/exercises/reserve';
  private baseUrl = 'http://localhost:8080/api/exercises';

  constructor(private http: HttpClient) {}

  reserveExercise(exerciseData: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this.apiUrl, exerciseData, { headers });
  }

  countExercisesByUserId(userId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/count/${userId}`);
  }
  
}
