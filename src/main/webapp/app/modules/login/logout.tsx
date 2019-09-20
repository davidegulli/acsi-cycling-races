import React from 'react';
import { connect } from 'react-redux';

import { IRootState } from 'app/shared/reducers';
import { logout } from 'app/shared/reducers/authentication';

export interface ILogoutProps extends StateProps, DispatchProps {
  idToken: string;
  logoutUrl: string;
}

export class Logout extends React.Component<ILogoutProps> {
  componentDidMount() {
    this.props.logout();
  }

  render() {
    const logoutUrl = this.props.logoutUrl;
    if (logoutUrl) {
      window.location.href = '/';
    }

    return (
      <div className="p-5">
        <h4>Logged out successfully!</h4>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  logoutUrl: storeState.authentication.logoutUrl,
  idToken: storeState.authentication.idToken
});

const mapDispatchToProps = { logout };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Logout);
