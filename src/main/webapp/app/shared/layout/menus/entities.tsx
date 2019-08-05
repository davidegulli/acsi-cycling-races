import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/acsi-team">
      Acsi Team
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/non-acsi-team">
      Non Acsi Team
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/athlete-black-list">
      Athlete Black List
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/file">
      File
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/contact">
      Contact
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/category">
      Category
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/race">
      Race
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/subscription-type">
      Subscription Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/path-type">
      Path Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/race-subscription">
      Race Subscription
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
