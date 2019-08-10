export interface IContact {
  id?: number;
  name?: string;
  email?: string;
  phone?: string;
  teamId?: number;
  raceId?: number;
}

export const defaultValue: Readonly<IContact> = {};
