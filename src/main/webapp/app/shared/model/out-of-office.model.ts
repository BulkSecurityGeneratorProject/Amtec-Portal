import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';

export interface IOutOfOffice {
  id?: number;
  start?: Moment;
  end?: Moment;
  description?: any;
  user?: IUser;
}

export const defaultValue: Readonly<IOutOfOffice> = {};
