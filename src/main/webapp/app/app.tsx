import 'react-toastify/dist/ReactToastify.css';
import './app.scss';

import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Card } from 'reactstrap';
import { BrowserRouter as Router } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import { hot } from 'react-hot-loader';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import Header from 'app/shared/layout/header/header';
import Footer from 'app/shared/layout/footer/footer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';
import LeftMenu from './shared/layout/menus/left-menu';
import { width } from '@fortawesome/free-solid-svg-icons/faUser';

const baseHref = document
  .querySelector('base')
  .getAttribute('href')
  .replace(/\/$/, '');

export interface IAppProps extends StateProps, DispatchProps {}

export const App = (props: IAppProps) => {
  useEffect(() => {
    props.getSession();
    props.getProfile();
  }, []);

  let leftMenu = null;

  if (props.isAuthenticated) {
    leftMenu = (
      <LeftMenu
        isAuthenticated={props.isAuthenticated}
        isAdmin={props.isAdmin}
        isAcsiAdmin={props.isAcsiAdmin}
        isTeamManager={props.isTeamManager}
        isSwaggerEnabled={props.isSwaggerEnabled}
      />
    );
  }

  return (
    <Router basename={baseHref}>
      <div className="app-container h-100">
        <ToastContainer position={toast.POSITION.TOP_LEFT} className="toastify-container" toastClassName="toastify-toast" />
        <header>
          <ErrorBoundary>
            <Header
              isAuthenticated={props.isAuthenticated}
              isAdmin={props.isAdmin}
              isAcsiAdmin={props.isAcsiAdmin}
              isTeamManager={props.isTeamManager}
              ribbonEnv={props.ribbonEnv}
              isInProduction={props.isInProduction}
              isSwaggerEnabled={props.isSwaggerEnabled}
            />
          </ErrorBoundary>
        </header>
        <div className="container h-100" style={props.isAdmin || props.isAcsiAdmin || props.isTeamManager ? { marginLeft: '250px' } : null}>
          {leftMenu}
          <main role="main" className="h-100">
            <div
              id="app-view-container"
              className={props.isAdmin || props.isAcsiAdmin || props.isTeamManager ? 'view-container py-4 px-4' : 'view-container py-4'}
            >
              <div className="container h-100">
                <Card className="jh-card">
                  <ErrorBoundary>
                    <AppRoutes />
                  </ErrorBoundary>
                </Card>
              </div>
            </div>
          </main>
        </div>
      </div>
    </Router>
  );
};

/*
        <footer>
          <Footer isAuthenticated={props.isAuthenticated} />
        </footer>


<div style={{ display: 'flex', width: '100%', alignItems: 'stretch' }}>
                {leftMenu}
                <div className="content">
                  <Card className="jh-card">
                    <ErrorBoundary>
                      <AppRoutes />
                    </ErrorBoundary>
                  </Card>
                </div>
              </div>
*/

const mapStateToProps = ({ authentication, applicationProfile }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN]),
  isAcsiAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ACSI_ADMIN]),
  isTeamManager: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.TEAM_MANAGER]),
  ribbonEnv: applicationProfile.ribbonEnv,
  isInProduction: applicationProfile.inProduction,
  isSwaggerEnabled: applicationProfile.isSwaggerEnabled
});

const mapDispatchToProps = { getSession, getProfile };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(hot(module)(App));
