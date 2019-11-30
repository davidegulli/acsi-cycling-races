import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubscriptionDiscount from './subscription-discount';
import SubscriptionDiscountDetail from './subscription-discount-detail';
import SubscriptionDiscountUpdate from './subscription-discount-update';
import SubscriptionDiscountDeleteDialog from './subscription-discount-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubscriptionDiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubscriptionDiscountUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubscriptionDiscountDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubscriptionDiscount} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SubscriptionDiscountDeleteDialog} />
  </>
);

export default Routes;
