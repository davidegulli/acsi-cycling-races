import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AcsiTeam from './acsi-team';
import AcsiTeamDetail from './acsi-team-detail';
import AcsiTeamUpdate from './acsi-team-update';
import AcsiTeamDeleteDialog from './acsi-team-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AcsiTeamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AcsiTeamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AcsiTeamDetail} />
      <ErrorBoundaryRoute path={match.url} component={AcsiTeam} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AcsiTeamDeleteDialog} />
  </>
);

export default Routes;
