import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './subscription-discount.reducer';
import { ISubscriptionDiscount } from 'app/shared/model/subscription-discount.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISubscriptionDiscountDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SubscriptionDiscountDetail extends React.Component<ISubscriptionDiscountDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { subscriptionDiscountEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            SubscriptionDiscount [<b>{subscriptionDiscountEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{subscriptionDiscountEntity.name}</dd>
            <dt>
              <span id="discount">Discount</span>
            </dt>
            <dd>{subscriptionDiscountEntity.discount}</dd>
            <dt>
              <span id="type">Type</span>
            </dt>
            <dd>{subscriptionDiscountEntity.type}</dd>
            <dt>
              <span id="expirationDate">Expiration Date</span>
            </dt>
            <dd>
              <TextFormat value={subscriptionDiscountEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>Subscription Type</dt>
            <dd>{subscriptionDiscountEntity.subscriptionTypeId ? subscriptionDiscountEntity.subscriptionTypeId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/subscription-discount" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/subscription-discount/${subscriptionDiscountEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ subscriptionDiscount }: IRootState) => ({
  subscriptionDiscountEntity: subscriptionDiscount.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubscriptionDiscountDetail);
