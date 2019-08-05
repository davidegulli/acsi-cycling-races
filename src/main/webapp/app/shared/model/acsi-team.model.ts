import { IRace } from 'app/shared/model/race.model';
import { IContact } from 'app/shared/model/contact.model';

export interface IAcsiTeam {
  id?: number;
  code?: string;
  name?: string;
  userId?: string;
  races?: IRace[];
  contacts?: IContact[];
}

export const defaultValue: Readonly<IAcsiTeam> = {};
