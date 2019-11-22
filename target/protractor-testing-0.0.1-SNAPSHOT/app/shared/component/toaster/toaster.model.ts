/**
 * @author Faizal Vasaya
 * A contract for Toasters
 */ export class Toaster {
  id?: number;
  state?: 'in' | 'out';
  type: ToasterType;
  title: ToasterTitle;
  message: string;
}

export enum ToasterType {
  Success = 'success',
  Info = 'info',
  Warning = 'warning',
  Error = 'danger'
}
export enum ToasterTitle {
  Success = 'success',
  Info = 'info',
  Warning = 'warning',
  Error = 'error'
}
