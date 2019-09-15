import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RaceSubscription from './race-subscription';
import RaceSubscriptionDetail from './race-subscription-detail';
import RaceSubscriptionUpdate from './race-subscription-update';
import RaceSubscriptionDeleteDialog from './race-subscription-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:raceId/new`} component={RaceSubscriptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RaceSubscriptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RaceSubscriptionDetail} />
      <ErrorBoundaryRoute path={match.url} component={RaceSubscription} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RaceSubscriptionDeleteDialog} />
  </>
);

export default Routes;
