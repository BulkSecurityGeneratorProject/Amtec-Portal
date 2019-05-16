import { IUser } from 'app/shared/model/user.model';
import { IRelease } from 'app/shared/model/release.model';

export const enum Territory {
  CETC_WEST = 'CETC WEST',
  HUDSON = 'HUDSON',
  SED = 'SED',
  NED = 'NED',
  CHICAGO = 'CHICAGO',
  PSCC = 'PSCC',
  UNIFIED = 'UNIFIED'
}

export const enum Priority {
  SHOWSTOPPER = 'SHOWSTOPPER',
  EMERGENCY = 'EMERGENCY',
  HIGH = 'HIGH',
  LOW = 'LOW',
  ROUTINE = 'ROUTINE',
  DEFER = 'DEFER'
}

export const enum Resolution {
  NEW = 'NEW',
  RELEASED = 'RELEASED',
  DUPLICATE = 'DUPLICATE',
  TESTED = 'TESTED',
  FIXED = 'FIXED',
  PARTIALLY_FIXED = 'PARTIALLY FIXED',
  REVIEWED = 'REVIEWED',
  CANNOT_REPRODUCE = 'CANNOT REPRODUCE'
}

export interface ISpr {
  id?: number;
  territory?: Territory;
  number?: number;
  fullNumber?: string;
  description?: any;
  priority?: Priority;
  resolution?: Resolution;
  jiraLink?: string;
  reviewerId?: number;
  user?: IUser;
  release?: IRelease;
}

export const defaultValue: Readonly<ISpr> = {};
