import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = props => (
  <div className="footer">
    <div className="container text-center" style={props.isAuthenticated ? { marginLeft: '265px' } : null}>
      <p className="text-muted">ACSI - Associazione di Cultura Sport e Tempo Libero - Via Montecatini, 5 00186 Roma</p>
      <p className="text-muted">Copyright © 2019 ACSI</p>
    </div>
  </div>
);

export default Footer;
