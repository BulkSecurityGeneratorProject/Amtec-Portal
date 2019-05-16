import { Moment } from 'moment';
import { ISpr } from 'app/shared/model/spr.model';

export const enum Territory {
  CETC_WEST = 'CETC WEST',
  HUDSON = 'HUDSON',
  SED = 'SED',
  NED = 'NED',
  CHICAGO = 'CHICAGO',
  PSCC = 'PSCC',
  UNIFIED = 'UNIFIED'
}

export interface IRelease {
  id?: number;
  date?: Moment;
  territory?: Territory;
  build?: number;
  releaseLetter?: string;
  prefixLetter?: string;
  databaseVersion?: string;
  wsVersion?: string;
  tmaVersion?: string;
  port?: number;
  newFeatures?: any;
  updatedFeatures?: any;
  current?: boolean;
  sprs?: ISpr[];
}

export const defaultValue: Readonly<IRelease> = {
  current: false
};
