import React from 'react';
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

export interface IRaceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RaceDetail extends React.Component<IRaceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { raceEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Race [<b>{raceEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{raceEntity.name}</dd>
            <dt>
              <span id="date">Date</span>
            </dt>
            <dd>
              <TextFormat value={raceEntity.date} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="location">Location</span>
            </dt>
            <dd>{raceEntity.location}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{raceEntity.description}</dd>
            <dt>
              <span id="info">Info</span>
            </dt>
            <dd>{raceEntity.info}</dd>
            <dt>
              <span id="address">Address</span>
            </dt>
            <dd>{raceEntity.address}</dd>
            <dt>
              <span id="latitude">Latitude</span>
            </dt>
            <dd>{raceEntity.latitude}</dd>
            <dt>
              <span id="longitude">Longitude</span>
            </dt>
            <dd>{raceEntity.longitude}</dd>
            <dt>
              <span id="rules">Rules</span>
            </dt>
            <dd>{raceEntity.rules}</dd>
            <dt>
              <span id="subscriptionExpirationDate">Subscription Expiration Date</span>
            </dt>
            <dd>
              <TextFormat value={raceEntity.subscriptionExpirationDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="attributes">Attributes</span>
            </dt>
            <dd>{raceEntity.attributes}</dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{raceEntity.status}</dd>
            <dt>Acsi Team</dt>
            <dd>{raceEntity.acsiTeamId ? raceEntity.acsiTeamId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/race" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/race/${raceEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
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
