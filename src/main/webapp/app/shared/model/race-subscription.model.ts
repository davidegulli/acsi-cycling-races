import { Moment } from 'moment';

export const enum GenderType {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

export const enum PaymentType {
  PAYPAL = 'PAYPAL',
  CREDIT_TRANSFER = 'CREDIT_TRANSFER'
}

export interface IRaceSubscription {
  id?: number;
  name?: string;
  surname?: string;
  birthDate?: string;
  birthPlace?: string;
  gender?: GenderType;
  taxCode?: string;
  email?: string;
  phone?: string;
  category?: string;
  subcriptionTypeId?: number;
  pathType?: number;
  teamId?: number;
  teamCode?: string;
  teamName?: string;
  athleteId?: string;
  date?: Moment;
  attribute?: string;
  paymentType?: PaymentType;
  paymentReceivedCode?: string;
  payed?: boolean;
  payedPrice?: number;
  raceId?: number;
  binaryPersonalIdDoc?: any;
  binaryPersonalIdDocContentType?: string;
  binaryMedicalCertificationDoc?: any;
  binaryMedicalCertificationDocContentType?: string;
}

export const defaultValue: Readonly<IRaceSubscription> = {
  payed: false
};
