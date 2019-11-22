export interface ICourse {
  id?: number;
  courseName?: string;
  courseFee?: number;
  courseDuration?: number;
}

export class Course implements ICourse {
  constructor(public id?: number, public courseName?: string, public courseFee?: number, public courseDuration?: number) {}
}
