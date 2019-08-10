import React from 'react';
import './no-element-found.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const noElementFound = () => {
  return (
    <div className="no-element-found-container">
      <div className="no-element-found">
        <FontAwesomeIcon icon="frown" color="gray" size="5x" />
        <span className="no-element-found-text">Non ci sono dati</span>
      </div>
    </div>
  );
};

export default noElementFound;
