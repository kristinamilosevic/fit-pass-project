import { Review } from "./Review";

export class Comment {
  id: number;
  text: string;
  createdAt: Date;
  reviews: Review[];

  constructor(id: number, text: string, createdAt: Date, reviews: Review[]) {
    this.id = id;
    this.text = text;
    this.createdAt = createdAt;
    this.reviews = reviews;
  }
}
