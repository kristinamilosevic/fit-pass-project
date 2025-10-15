import { User } from "./User";
import { Facility } from "./Facility";

export class Exercise {
  id: number;
  fromTime: Date;
  untilTime: Date;
  user: User;
  facility: Facility;

  constructor(id: number, fromTime: Date, untilTime: Date, user: User, facility: Facility) {
    this.id = id;
    this.fromTime = fromTime;
    this.untilTime = untilTime;
    this.user = user;
    this.facility = facility;
  }
}
