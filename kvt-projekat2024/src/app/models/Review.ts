import { User } from "./User";
import { Comment } from "./Comment";
import { Facility } from "./Facility";
import { Rate } from "./Rate";

export class Review {
    id: number;
    createdAt: Date;
    exerciseCount: number;
    hidden: boolean;
    isActive: boolean; 
    user: User;
    facilityName: string; // Promenjen naziv
    rate?: {
      equipment: number;
      staff: number;
      hygiene: number;
      space: number;
    };
    commentText?: string;
  
    constructor(
      id: number,
      createdAt: Date = new Date(),
      exerciseCount: number = 0,
      hidden: boolean = false,
      isActive: boolean = true,
      user: User,
      facilityName: string, // Promenjen naziv
      rate: Rate,
      commentText?: string
    ) {
      this.id = id;
      this.createdAt = createdAt;
      this.exerciseCount = exerciseCount;
      this.hidden = hidden;
      this.isActive = isActive;
      this.user = user;
      this.facilityName = facilityName;
      this.rate = rate;
      this.commentText = commentText;
    }
  }