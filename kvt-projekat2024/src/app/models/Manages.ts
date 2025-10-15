import { User } from "./User";
import { Facility } from "./Facility";

export class Manages {
  id: number;
  startDate: Date;
  endDate: Date;
  user: User;
  facility: Facility;

  constructor(id: number, startDate: Date, endDate: Date, user: User, facility: Facility) {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.user = user;
    this.facility = facility;
  }
}
