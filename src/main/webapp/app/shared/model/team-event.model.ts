import { Moment } from 'moment';

export interface ITeamEvent {
  id?: number;
  start?: Moment;
  end?: Moment;
  description?: any;
}

export const defaultValue: Readonly<ITeamEvent> = {};
