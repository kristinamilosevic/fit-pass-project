import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountRequestService {

  private baseUrl = 'http://localhost:8080/api/account-requests';

  constructor(private http: HttpClient) {}

  getAllRequests(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  createRequest(accountRequest: any): Observable<any> {
    return this.http.post(this.baseUrl, accountRequest);
  }

  deleteRequest(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
