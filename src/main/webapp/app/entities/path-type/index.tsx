import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PathType from './path-type';
import PathTypeDetail from './path-type-detail';
import PathTypeUpdate from './path-type-update';
import PathTypeDeleteDialog from './path-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PathTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PathTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PathTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={PathType} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PathTypeDeleteDialog} />
  </>
);

export default Routes;
