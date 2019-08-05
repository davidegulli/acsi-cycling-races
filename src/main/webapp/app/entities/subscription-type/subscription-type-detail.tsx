import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-type.reducer';
import { ISubscriptionType } from 'app/shared/model/subscription-type.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionTypeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SubscriptionTypeDetail extends React.Component<ISubscriptionTypeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { subscriptionTypeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            SubscriptionType [<b>{subscriptionTypeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{subscriptionTypeEntity.name}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{subscriptionTypeEntity.description}</dd>
            <dt>
              <span id="rules">Rules</span>
            </dt>
            <dd>{subscriptionTypeEntity.rules}</dd>
            <dt>
              <span id="price">Price</span>
            </dt>
            <dd>{subscriptionTypeEntity.price}</dd>
          </dl>
          <Button tag={Link} to="/entity/subscription-type" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/subscription-type/${subscriptionTypeEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ subscriptionType }: IRootState) => ({
  subscriptionTypeEntity: subscriptionType.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubscriptionTypeDetail);
