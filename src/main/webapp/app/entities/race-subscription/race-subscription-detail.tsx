import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './race-subscription.reducer';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRaceSubscriptionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RaceSubscriptionDetail extends React.Component<IRaceSubscriptionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { raceSubscriptionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RaceSubscription [<b>{raceSubscriptionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{raceSubscriptionEntity.name}</dd>
            <dt>
              <span id="surname">Surname</span>
            </dt>
            <dd>{raceSubscriptionEntity.surname}</dd>
            <dt>
              <span id="birthDate">Birth Date</span>
            </dt>
            <dd>{raceSubscriptionEntity.birthDate}</dd>
            <dt>
              <span id="birthPlace">Birth Place</span>
            </dt>
            <dd>{raceSubscriptionEntity.birthPlace}</dd>
            <dt>
              <span id="gender">Gender</span>
            </dt>
            <dd>{raceSubscriptionEntity.gender}</dd>
            <dt>
              <span id="taxCode">Tax Code</span>
            </dt>
            <dd>{raceSubscriptionEntity.taxCode}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{raceSubscriptionEntity.email}</dd>
            <dt>
              <span id="phone">Phone</span>
            </dt>
            <dd>{raceSubscriptionEntity.phone}</dd>
            <dt>
              <span id="category">Category</span>
            </dt>
            <dd>{raceSubscriptionEntity.category}</dd>
            <dt>
              <span id="subcriptionTypeId">Subcription Type Id</span>
            </dt>
            <dd>{raceSubscriptionEntity.subcriptionTypeId}</dd>
            <dt>
              <span id="pathType">Path Type</span>
            </dt>
            <dd>{raceSubscriptionEntity.pathType}</dd>
            <dt>
              <span id="teamId">Team Id</span>
            </dt>
            <dd>{raceSubscriptionEntity.teamId}</dd>
            <dt>
              <span id="athleteId">Athlete Id</span>
            </dt>
            <dd>{raceSubscriptionEntity.athleteId}</dd>
            <dt>
              <span id="date">Date</span>
            </dt>
            <dd>
              <TextFormat value={raceSubscriptionEntity.date} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="paymentType">Payment Type</span>
            </dt>
            <dd>{raceSubscriptionEntity.paymentType}</dd>
            <dt>
              <span id="payed">Payed</span>
            </dt>
            <dd>{raceSubscriptionEntity.payed ? 'true' : 'false'}</dd>
            <dt>
              <span id="payedPrice">Payed Price</span>
            </dt>
            <dd>{raceSubscriptionEntity.payedPrice}</dd>
            <dt>Race</dt>
            <dd>{raceSubscriptionEntity.raceId ? raceSubscriptionEntity.raceId : ''}</dd>
            <dt>Race</dt>
            <dd>{raceSubscriptionEntity.raceId ? raceSubscriptionEntity.raceId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/race-subscription" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/race-subscription/${raceSubscriptionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ raceSubscription }: IRootState) => ({
  raceSubscriptionEntity: raceSubscription.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceSubscriptionDetail);
