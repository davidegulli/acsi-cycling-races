import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RaceType from './race-type';
import RaceTypeDetail from './race-type-detail';
import RaceTypeUpdate from './race-type-update';
import RaceTypeDeleteDialog from './race-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RaceTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RaceTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RaceTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={RaceType} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RaceTypeDeleteDialog} />
  </>
);

export default Routes;
