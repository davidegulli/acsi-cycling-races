import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionType from './subscription-type';
import SubscriptionTypeDetail from './subscription-type-detail';
import SubscriptionTypeUpdate from './subscription-type-update';
import SubscriptionTypeDeleteDialog from './subscription-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionType} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SubscriptionTypeDeleteDialog} />
  </>
);

export default Routes;
