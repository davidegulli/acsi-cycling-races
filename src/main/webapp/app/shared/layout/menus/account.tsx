import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem, NavItem, NavLink } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink as Link } from 'react-router-dom';

import { NavDropdown } from './menu-components';

const accountMenuItemsAuthenticated = (
  <NavDropdown icon="user" name="Account" id="account-menu">
    <MenuItem icon="wrench" to="/account/settings">
      Settings
    </MenuItem>
    <MenuItem icon="lock" to="/account/password">
      Password
    </MenuItem>
    <MenuItem icon="sign-out-alt" to="/logout">
      Sign out
    </MenuItem>
  </NavDropdown>
);

const accountMenuItems = (
  <NavItem>
    <NavLink tag={Link} to="/login" className="d-flex align-items-center">
      <FontAwesomeIcon icon="sign-in-alt" />
      <span>Sign in</span>
    </NavLink>
  </NavItem>
);

/*
    <MenuItem id="login-item" icon="sign-in-alt" to="/login">
      Sign in
    </MenuItem>

    <MenuItem icon="cloud" to="/account/sessions">
      Sessions
    </MenuItem>
    <MenuItem icon="sign-in-alt" to="/register">
      Register
    </MenuItem>

    <NavItem>
    <NavLink tag={Link} to="/login" className="d-flex align-items-center">
      <FontAwesomeIcon icon="sign-in-alt" />
      <span>Sign in</span>
    </NavLink>
  </NavItem>
*/

export const AccountMenu = ({ isAuthenticated = false }) => (isAuthenticated ? accountMenuItemsAuthenticated : accountMenuItems);

export default AccountMenu;
