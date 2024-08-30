import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

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

  constructor(private http: HttpClient) {}


  onSubmit() {
    this.http.post('http://localhost:8080/api/account-requests', this.accountRequest)
      .subscribe(response => {
        console.log('Account request submitted:', response);
      });
  }
}
