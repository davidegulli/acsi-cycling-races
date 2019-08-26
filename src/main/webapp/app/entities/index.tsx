import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AcsiTeam from './acsi-team';
import NonAcsiTeam from './non-acsi-team';
import AthleteBlackList from './athlete-black-list';
import File from './file';
import Contact from './contact';
import Category from './category';
import Race from './race';
import SubscriptionType from './subscription-type';
import PathType from './path-type';
import RaceSubscription from './race-subscription';
import RaceType from './race-type';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/acsi-team`} component={AcsiTeam} />
      <ErrorBoundaryRoute path={`${match.url}/non-acsi-team`} component={NonAcsiTeam} />
      <ErrorBoundaryRoute path={`${match.url}/athlete-black-list`} component={AthleteBlackList} />
      <ErrorBoundaryRoute path={`${match.url}/file`} component={File} />
      <ErrorBoundaryRoute path={`${match.url}/contact`} component={Contact} />
      <ErrorBoundaryRoute path={`${match.url}/category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}/race`} component={Race} />
      <ErrorBoundaryRoute path={`${match.url}/subscription-type`} component={SubscriptionType} />
      <ErrorBoundaryRoute path={`${match.url}/path-type`} component={PathType} />
      <ErrorBoundaryRoute path={`${match.url}/race-subscription`} component={RaceSubscription} />
      <ErrorBoundaryRoute path={`${match.url}/race-type`} component={RaceType} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
