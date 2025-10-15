import { Facility } from "./Facility";
import { DayOfWeek } from "./DayOfWeek";

export class WorkDay {
  id: number;
  validFrom: Date;
  day: DayOfWeek;
  fromTime: string;
  untilTime: string;
  facility?: Facility;

  constructor(id: number, validFrom: Date, day: DayOfWeek, fromTime: string, untilTime: string, facility: Facility) {
    this.id = id;
    this.validFrom = validFrom;
    this.day = day;
    this.fromTime = fromTime;
    this.untilTime = untilTime;
    this.facility = facility;
  }
}
