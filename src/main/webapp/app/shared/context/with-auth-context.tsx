import React from 'react';
import AuthContext from './auth-context';

const withAuthContext = Component => props => (
  <AuthContext.Consumer>
    {({ isAuthenticated, isAcsiAdmin, isAdmin, isTeamManager }) => (
      <Component {...props} isAuthenticated={isAuthenticated} isAcsiAdmin={isAcsiAdmin} isAdmin={isAdmin} isTeamManager={isTeamManager} />
    )}
  </AuthContext.Consumer>
);

export default withAuthContext;
