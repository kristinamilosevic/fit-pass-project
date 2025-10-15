import { Review } from "./Review";
import { Exercise } from "./Exercise";
import { Manages } from "./Manages";
import { Image } from "./Image";
import { Discipline } from "./Discipline";
import { WorkDay } from "./WorkDay";

export class Facility {
  id: number;
  name: string;
  description: string;
  createdAt: Date;
  address: string;
  city: string;
  totalRating: number;
  active: boolean;
  reviews: Review[];
  exercises: Exercise[];
  manages: Manages[];
  images: Image[];
  disciplines: Discipline[];
  workDays: WorkDay[];

  constructor(
    id: number,
    name: string,
    description: string,
    createdAt: Date,
    address: string,
    city: string,
    totalRating: number,
    active: boolean,
    reviews: Review[],
    exercises: Exercise[],
    manages: Manages[],
    images: Image[],
    disciplines: Discipline[],
    workDays: WorkDay[]
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
    this.address = address;
    this.city = city;
    this.totalRating = totalRating;
    this.active = active;
    this.reviews = reviews;
    this.exercises = exercises;
    this.manages = manages;
    this.images = images;
    this.disciplines = disciplines;
    this.workDays = workDays;
  }
}
