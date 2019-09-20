import React from 'react';
import { Nav } from 'reactstrap';
import { RacesMenu } from './races';
import { TeamsMenu } from './teams';
import { AthletesMenu } from './athletes';

import './left-menu.scss';

export interface ILeftMenuProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  isSwaggerEnabled: boolean;
  isAcsiAdmin: boolean;
  isTeamManager: boolean;
}

const LeftMenu = (props: ILeftMenuProps) => (
  <div className="left-menu">
    <Nav id="left-menu" vertical>
      <RacesMenu isAcsiAdmin={props.isAcsiAdmin} isAdmin={props.isAdmin} />
      {props.isAcsiAdmin || props.isAdmin ? <TeamsMenu /> : null}
      {props.isAcsiAdmin || props.isAdmin ? <AthletesMenu /> : null}
    </Nav>
  </div>
);

export default LeftMenu;
