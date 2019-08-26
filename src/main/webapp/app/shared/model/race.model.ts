import { Moment } from 'moment';
import { IContact } from 'app/shared/model/contact.model';
import { IFile } from 'app/shared/model/file.model';
import { IPathType } from 'app/shared/model/path-type.model';
import { ISubscriptionType } from 'app/shared/model/subscription-type.model';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
import { IRaceType } from 'app/shared/model/race-type.model';

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
  types?: IRaceType[];
  acsiTeamId?: number;
}

export const defaultValue: Readonly<IRace> = {};
