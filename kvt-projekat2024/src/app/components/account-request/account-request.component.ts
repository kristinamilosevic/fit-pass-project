import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account-request',
  standalone: true,
  templateUrl: './account-request.component.html',
  styleUrls: ['./account-request.component.css'],
  imports: [CommonModule, FormsModule]
})
export class AccountRequestComponent{

  accountRequest = {
    email: '',
    address: ''
  };

  accountRequests: any[] = [];

  constructor(private http: HttpClient, private router: Router) {}


  onSubmit() {
    this.http.post('http://localhost:8080/api/account-requests', this.accountRequest)
      .subscribe(response => {
        console.log('Account request submitted:', response);
        alert('Account request has been successfully submitted!');
        this.router.navigate(['/login']);
      });
  }
}
