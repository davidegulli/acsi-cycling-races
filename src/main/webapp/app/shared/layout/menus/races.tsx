import './left-menu.scss';

import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavItem, NavLink } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface IRaceMenu {
  isAdmin: boolean;
  isAcsiAdmin: boolean;
}

export const RacesMenu = (props: IRaceMenu) => (
  <div className="left-menu-section">
    <span className="left-menu-section-title">Gare</span>
    <NavItem>
      <NavLink tag={Link} to="/entity/race" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="flag-checkered" />
        <span>Gestione Gare</span>
      </NavLink>
      {props.isAcsiAdmin || props.isAdmin ? (
        <NavLink tag={Link} to="/entity/race-type" className="d-flex align-items-center left-menu-item">
          <FontAwesomeIcon icon="bars" />
          <span>Discipline</span>
        </NavLink>
      ) : null}
    </NavItem>
  </div>
);
