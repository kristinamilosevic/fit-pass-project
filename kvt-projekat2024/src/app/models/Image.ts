import { User } from "./User";
import { Facility } from "./Facility";

export class Image {
  id: number;
  path: string;
  user: User;
  facility: Facility;

  constructor(
    id: number,
    path: string,
    user: User,
    facility: Facility
  ) {
    this.id = id;
    this.path = path;
    this.user = user;
    this.facility = facility;
  }
}
