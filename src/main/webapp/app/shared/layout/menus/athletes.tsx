import './left-menu.scss';

import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavItem, NavLink } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const AthletesMenu = props => (
  <div className="left-menu-section">
    <span className="left-menu-section-title">Atleti</span>
    <NavItem>
      <NavLink tag={Link} to="/entity/category" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="th-list" />
        <span>Categorie</span>
      </NavLink>
    </NavItem>
    <NavItem>
      <NavLink tag={Link} to="/entity/athlete-black-list" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="ban" />
        <span>Blacklist</span>
      </NavLink>
    </NavItem>
    <NavItem>
      <NavLink tag={Link} to="/entity/file" className="d-flex align-items-center left-menu-item">
        <FontAwesomeIcon icon="ban" />
        <span>File</span>
      </NavLink>
    </NavItem>
  </div>
);
