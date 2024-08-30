import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = 'http://localhost:8080/api/auth/authenticate';  
  private logoutUrl = 'http://localhost:8080/api/auth/logout';
  private apiUrl = 'http://localhost:8080/api/users'; 


  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    const body = { email, password };  
    console.log(body);
    return this.http.post(this.loginUrl, body, { responseType: 'text' });
  }

  logout() {
    const token = localStorage.getItem('authToken');
    if (token) {
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
      this.http.post(this.logoutUrl, {}, { headers, responseType: 'text' }).subscribe(
        response => {
          localStorage.removeItem('authToken');
          localStorage.removeItem('userRole');
          localStorage.removeItem('userEmail');
          alert('Uspesno ste se izlogovali.');
        },
        error => {
          console.error('Greška pri odjavi:', error);
        }
      );
    }
  }

  getUserRole(): string | null {
    return localStorage.getItem('userRole');
  }

  getUserTypeByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/user-type?email=${email}`);
  }
}
