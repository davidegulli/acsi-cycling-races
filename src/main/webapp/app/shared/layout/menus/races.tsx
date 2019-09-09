import './left-menu.scss';

import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavItem, NavLink } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const RacesMenu = props => (
  <div className="left-menu-section">
    <span className="left-menu-section-title">Gare</span>
    <NavItem>
      <NavLink tag={Link} to="/entity/race" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="flag-checkered" />
        <span>Gestione Gare</span>
      </NavLink>
      <NavLink tag={Link} to="/entity/race-type" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="bars" />
        <span>Discipline</span>
      </NavLink>
    </NavItem>
  </div>
);
