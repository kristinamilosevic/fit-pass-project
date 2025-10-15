import { Facility } from "./Facility";

export class Discipline {
  id: number;
  name: string;
  facility?: Facility;

  constructor(id: number, name: string, facility: Facility) {
    this.id = id;
    this.name = name;
    this.facility = facility;
  }
}
