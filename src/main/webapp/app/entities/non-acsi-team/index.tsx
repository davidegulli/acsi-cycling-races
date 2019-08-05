import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import NonAcsiTeam from './non-acsi-team';
import NonAcsiTeamDetail from './non-acsi-team-detail';
import NonAcsiTeamUpdate from './non-acsi-team-update';
import NonAcsiTeamDeleteDialog from './non-acsi-team-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NonAcsiTeamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NonAcsiTeamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NonAcsiTeamDetail} />
      <ErrorBoundaryRoute path={match.url} component={NonAcsiTeam} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={NonAcsiTeamDeleteDialog} />
  </>
);

export default Routes;
