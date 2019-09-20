import './home.scss';

import React from 'react';
import { connect } from 'react-redux';
import Events from '../events';

import { IRootState } from 'app/shared/reducers';
import { Box } from '@material-ui/core';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const { account } = props;

  return (
    <Box>
      <ErrorBoundaryRoute path="/" exact component={Events} />
    </Box>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
