export interface ISubscriptionType {
  id?: number;
  name?: string;
  description?: string;
  rules?: string;
  price?: number;
  raceId?: number;
}

export const defaultValue: Readonly<ISubscriptionType> = {};
