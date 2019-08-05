import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AthleteBlackList from './athlete-black-list';
import AthleteBlackListDetail from './athlete-black-list-detail';
import AthleteBlackListUpdate from './athlete-black-list-update';
import AthleteBlackListDeleteDialog from './athlete-black-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AthleteBlackListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AthleteBlackListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AthleteBlackListDetail} />
      <ErrorBoundaryRoute path={match.url} component={AthleteBlackList} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AthleteBlackListDeleteDialog} />
  </>
);

export default Routes;
