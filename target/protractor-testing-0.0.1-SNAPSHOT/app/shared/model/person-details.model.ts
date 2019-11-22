export interface IPersonDetails {
  id?: number;
  personName?: string;
  personId?: number;
  personAddress?: string;
}

export class PersonDetails implements IPersonDetails {
  constructor(public id?: number, public personName?: string, public personId?: number, public personAddress?: string) {}
}
