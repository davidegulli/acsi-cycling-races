import './race.scss';

import React from 'react';
import { Button } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { Link } from 'react-router-dom';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import moment from 'moment';

export interface IRaceHeader {
  entity: any;
  showButton: boolean;
}

const raceHeader = (props: IRaceHeader) => {
  const { entity, showButton } = props;

  const coverImage = {
    backgroundImage: 'url(' + entity.binaryCoverUrl + ')',
    backgroundRepeat: 'no-repeat',
    backgroundColor: '#fff',
    backgroundSize: '100% 100%'
  };

  let button = null;

  if (showButton && moment().diff(entity.subscriptionExpirationDate) < 0) {
    button = (
      <Button tag={Link} to={`/subscription/${entity.id}`} className="race-subscription-button">
        Iscriviti Ora
      </Button>
    );
  }

  return (
    <div style={coverImage} className="cover-image">
      <div className="race-main-data">
        <div>
          <span>
            <img src={entity.binaryLogoUrl} className="race-logo-image" />
          </span>
          <span className="race-main-data-title">{entity.name}</span>
        </div>
        <div className="pl-2">
          <div>{entity.description}</div>
          <div>{entity.location}</div>
          <div>
            <TextFormat value={entity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </div>
          {button}
        </div>
      </div>
    </div>
  );
};

export default raceHeader;
