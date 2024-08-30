import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin/admin.service';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AdminComponent implements OnInit {

  accountRequests: any[] = [];
  pendingRequests: any[] = [];
  rejectionReason: string = '';

  constructor(private adminService: AdminService, private http: HttpClient) {}

  ngOnInit() {
    this.getPendingAccountRequests();
  }

  // Dohvati sve zahteve
  getPendingAccountRequests() {
    this.http.get<any[]>('http://localhost:8080/api/account-requests')
      .subscribe(data => {
        this.accountRequests = data;
        // Filtriraj samo PENDING zahteve
        this.pendingRequests = this.accountRequests.filter(request => request.status === 'PENDING');
      });
  }

  // Odobri zahtev
  approveRequest(id: number) {
    this.adminService.approveAccountRequest(id).subscribe(() => {
      console.log('Zahtev odobren');
      this.getPendingAccountRequests();  // Osvježi listu nakon akcije
    });
  }

  // Odbij zahtev
  rejectRequest(id: number) {
    if (this.rejectionReason.trim() === '') {
      alert('Unesite razlog odbijanja.');
      return;
    }

    this.adminService.rejectAccountRequest(id, this.rejectionReason).subscribe(() => {
      console.log('Zahtev odbijen');
      this.getPendingAccountRequests();  // Osvježi listu nakon akcije
    });
  }
}
