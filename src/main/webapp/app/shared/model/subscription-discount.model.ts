import { Moment } from 'moment';

export const enum DiscountType {
  PERCENT = 'PERCENT',
  AMOUNT = 'AMOUNT'
}

export interface ISubscriptionDiscount {
  id?: number;
  name?: string;
  discount?: number;
  type?: DiscountType;
  expirationDate?: Moment;
  subscriptionTypeId?: number;
}

export const defaultValue: Readonly<ISubscriptionDiscount> = {};
