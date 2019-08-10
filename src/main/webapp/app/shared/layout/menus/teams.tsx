import './left-menu.scss';

import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavItem, NavLink } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const TeamsMenu = props => (
  <div className="left-menu-section">
    <span className="left-menu-section-title">Associazioni</span>
    <NavItem>
      <NavLink tag={Link} to="/entity/acsi-team" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="users" />
        <span>Affiliate</span>
      </NavLink>
    </NavItem>
    <NavItem>
      <NavLink tag={Link} to="/entity/non-acsi-team" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="user-friends" />
        <span>Non Affiliate</span>
      </NavLink>
    </NavItem>
  </div>
);
