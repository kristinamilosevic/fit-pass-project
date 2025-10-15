import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-account-request-list',
  standalone: true,
  templateUrl: './account-request-list.component.html',
  styleUrls: ['./account-request-list.component.css'],
  imports: [CommonModule] 
})
export class AccountRequestListComponent implements OnInit {

  accountRequests: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.getAllAccountRequests();
  }

  getAllAccountRequests() {
    this.http.get<any[]>('http://localhost:8080/api/account-requests')
      .subscribe(data => {
        this.accountRequests = data;
      });
  }

  deleteRequest(id: number) {
    this.http.delete(`http://localhost:8080/api/account-requests/${id}`)
      .subscribe(() => {
        console.log('Account request deleted');
        this.getAllAccountRequests();  
      });
  }
}
