import { Review } from "./Review";

export class Rate {
  id: number;
  equipment: number;
  staff: number;
  hygiene: number;
  space: number;
  reviews: Review[];

  constructor(id: number, equipment: number, staff: number, hygiene: number, space: number, reviews: Review[]) {
    this.id = id;
    this.equipment = equipment;
    this.staff = staff;
    this.hygiene = hygiene;
    this.space = space;
    this.reviews = reviews;
  }
}
