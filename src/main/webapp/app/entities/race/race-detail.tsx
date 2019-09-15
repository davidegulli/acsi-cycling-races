import './race.scss';

import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './race.reducer';
import { IRace } from 'app/shared/model/race.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import RaceHeader from './race-header';

export interface IRaceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RaceDetail extends React.Component<IRaceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { raceEntity } = this.props;

    return (
      <Fragment>
        <RaceHeader entity={raceEntity} showButton />
        <div className="race-secondary-data container-fluid">
          <div className="row">
            <div className="col-12">
              <h4 className="sheet-title">Informazioni Utili</h4>
              <div>
                <span className="race-secondary-data-label">Indirizzo:</span>
                <span>{raceEntity.address}</span>
              </div>
              <div>
                <span className="race-secondary-data-label">Disciplina:</span>
                <span>{raceEntity.typeName}</span>
              </div>
              <div>
                <span className="race-secondary-data-label">Scadenza Iscrizioni:</span>
                <span>
                  <TextFormat value={raceEntity.subscriptionExpirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
                </span>
              </div>
              <div>
                <span className="race-secondary-data-label">Info:</span>
                <span>{raceEntity.info}</span>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col-12">
              <h4 className="sheet-title">Contatti Organizzazione</h4>
              <div className="race-secondary-data-label-no-text">
                <FontAwesomeIcon icon="user" />
                <span className="ml-2">{raceEntity.contactName}</span>
              </div>
              <div className="race-secondary-data-label-no-text">
                <FontAwesomeIcon icon="phone" />
                <span className="ml-2">{raceEntity.contactPhone}</span>
              </div>
              <div className="race-secondary-data-label-no-text">
                <FontAwesomeIcon icon="envelope" />
                <span className="ml-2">{raceEntity.contactEmail}</span>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col-6">
              <h4 className="sheet-title">Regolamento</h4>
              <div>{raceEntity.rules}</div>
            </div>
            <div className="col-6">
              <h4 className="sheet-title">Percorso</h4>
              <img src={raceEntity.binaryPathMapUrl} className="race-path-map-image" />
            </div>
          </div>
        </div>
      </Fragment>
    );
  }
}

const mapStateToProps = ({ race }: IRootState) => ({
  raceEntity: race.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceDetail);
