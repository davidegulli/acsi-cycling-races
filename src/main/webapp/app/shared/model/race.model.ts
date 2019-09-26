import { Moment } from 'moment';
import { IContact } from 'app/shared/model/contact.model';
import { IFile } from 'app/shared/model/file.model';
import { IPathType } from 'app/shared/model/path-type.model';
import { ISubscriptionType } from 'app/shared/model/subscription-type.model';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';

export const enum RaceStatus {
  PUBLISHED = 'PUBLISHED',
  DRAFT = 'DRAFT',
  CANCELED = 'CANCELED',
  UNPUBLISHED = 'UNPUBLISHED'
}

export interface IRace {
  id?: number;
  name?: string;
  date?: Moment;
  location?: string;
  description?: string;
  info?: string;
  address?: string;
  latitude?: number;
  longitude?: number;
  rules?: string;
  subscriptionExpirationDate?: Moment;
  attributes?: string;
  status?: RaceStatus;
  contacts?: IContact[];
  attachments?: IFile[];
  pathTypes?: IPathType[];
  subscriptionTypes?: ISubscriptionType[];
  subscriptions?: IRaceSubscription[];
  typeId?: number;
  typeName?: string;
  acsiTeamId?: number;
  contactName?: string;
  contactEmail?: string;
  contactPhone?: string;
  binaryLogoImage?: any;
  binaryLogoUrl?: string;
  binaryCoverImage?: any;
  binaryCoverUrl?: string;
  binaryPathMapImage?: any;
  binaryPathMapUrl?: string;
}

export const defaultValue: Readonly<IRace> = {
  subscriptionTypes: [],
  pathTypes: [],
  attachments: [],
  subscriptions: [],
  contacts: []
};
