export const enum GenderType {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

export interface ICategory {
  id?: number;
  name?: string;
  alias?: string;
  gender?: GenderType;
  minAge?: number;
  maxAge?: number;
}

export const defaultValue: Readonly<ICategory> = {};
