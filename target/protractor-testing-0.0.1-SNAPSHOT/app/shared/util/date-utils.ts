import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

export class DateUtils {
  public toIsoString(date: NgbDateStruct): string {
    const utcDateObject = new Date(Date.UTC(date.year, date.month - 1, date.day));
    const localDateObject = new Date(utcDateObject.getTime() + utcDateObject.getTimezoneOffset() * 60000);

    return localDateObject.toISOString();
  }

  public fromIsoString(date: Date | string): NgbDateStruct | null {
    if (!date) {
      return null;
    }

    let dateObject;

    if (date instanceof Date) {
      dateObject = date;
    } else {
      dateObject = new Date(date);
    }

    return { day: dateObject.getUTCDate(), month: dateObject.getUTCMonth() + 1, year: dateObject.getUTCFullYear() };
  }
  /**
   * Convert ngbDateStruct to string with YYYY-MM-DDTHH:MM:SSZ. Example: 2018-12-21T06:35:28.000Z
   */
  toIsoStringWithTime(date: NgbDateStruct): string | null {
    if (!date) {
      return null;
    }
    const currentTime = new Date();
    const utcDateObject = new Date(
      Date.UTC(date.year, date.month - 1, date.day, currentTime.getHours(), currentTime.getMinutes(), currentTime.getSeconds())
    );
    const zoneDateObject = new Date(utcDateObject.getTime() + utcDateObject.getTimezoneOffset() * 60000);
    return zoneDateObject.toISOString();
  }
}
