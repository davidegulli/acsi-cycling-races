export interface ISubscriptionType {
  id?: number;
  name?: string;
  description?: string;
  rules?: string;
  price?: number;
}

export const defaultValue: Readonly<ISubscriptionType> = {};
