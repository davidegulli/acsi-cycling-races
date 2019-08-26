import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
import sessions, { SessionsState } from 'app/modules/account/sessions/sessions.reducer';
// prettier-ignore
import acsiTeam, {
  AcsiTeamState
} from 'app/entities/acsi-team/acsi-team.reducer';
// prettier-ignore
import nonAcsiTeam, {
  NonAcsiTeamState
} from 'app/entities/non-acsi-team/non-acsi-team.reducer';
// prettier-ignore
import athleteBlackList, {
  AthleteBlackListState
} from 'app/entities/athlete-black-list/athlete-black-list.reducer';
// prettier-ignore
import file, {
  FileState
} from 'app/entities/file/file.reducer';
// prettier-ignore
import contact, {
  ContactState
} from 'app/entities/contact/contact.reducer';
// prettier-ignore
import category, {
  CategoryState
} from 'app/entities/category/category.reducer';
// prettier-ignore
import race, {
  RaceState
} from 'app/entities/race/race.reducer';
// prettier-ignore
import subscriptionType, {
  SubscriptionTypeState
} from 'app/entities/subscription-type/subscription-type.reducer';
// prettier-ignore
import pathType, {
  PathTypeState
} from 'app/entities/path-type/path-type.reducer';
// prettier-ignore
import raceSubscription, {
  RaceSubscriptionState
} from 'app/entities/race-subscription/race-subscription.reducer';
// prettier-ignore
import raceType, {
  RaceTypeState
} from 'app/entities/race-type/race-type.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly sessions: SessionsState;
  readonly acsiTeam: AcsiTeamState;
  readonly nonAcsiTeam: NonAcsiTeamState;
  readonly athleteBlackList: AthleteBlackListState;
  readonly file: FileState;
  readonly contact: ContactState;
  readonly category: CategoryState;
  readonly race: RaceState;
  readonly subscriptionType: SubscriptionTypeState;
  readonly pathType: PathTypeState;
  readonly raceSubscription: RaceSubscriptionState;
  readonly raceType: RaceTypeState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  sessions,
  acsiTeam,
  nonAcsiTeam,
  athleteBlackList,
  file,
  contact,
  category,
  race,
  subscriptionType,
  pathType,
  raceSubscription,
  raceType,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
