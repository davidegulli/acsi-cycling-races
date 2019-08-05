export interface IPathType {
  id?: number;
  name?: string;
  description?: string;
  distance?: string;
  raceId?: number;
}

export const defaultValue: Readonly<IPathType> = {};
