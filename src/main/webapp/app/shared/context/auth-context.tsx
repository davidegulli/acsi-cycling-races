import React from 'react';

const authContext = React.createContext({
  isAuthenticated: false,
  isAdmin: false,
  isAcsiAdmin: false,
  isTeamManager: false,
  acsiTeam: {}
});

export default authContext;
