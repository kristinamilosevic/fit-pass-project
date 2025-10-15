import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../../models/Review'; 

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private baseUrl = 'http://localhost:8080/api/reviews';
  private apiUrl = 'http://localhost:8080/api/reviews/create'; 
  constructor(private http: HttpClient) { }

  createReview(reviewData: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, reviewData);
  }

  getReviewsByUserId(userId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.baseUrl}/all-by-user?userId=${userId}`);
  }

  getReviewById(id: number): Observable<Review> {
    return this.http.get<Review>(`${this.baseUrl}/${id}`);
  }
}
