export class AccountRequest {
    id: number;
    email: string;
    create_at: Date;
    address: string;
    status: RequestStatus;
    rejection_reason?: string;  
  
    constructor(id: number, email: string, create_at: Date, address: string, status: RequestStatus, rejection_reason?: string) {
      this.id = id;
      this.email = email;
      this.create_at = create_at;
      this.address = address;
      this.status = status;
      this.rejection_reason = rejection_reason;
    }
  }
  