import { Moment } from 'moment';

export interface IAthleteBlackList {
  id?: number;
  name?: string;
  surname?: string;
  birthDate?: Moment;
  taxCode?: string;
}

export const defaultValue: Readonly<IAthleteBlackList> = {};
