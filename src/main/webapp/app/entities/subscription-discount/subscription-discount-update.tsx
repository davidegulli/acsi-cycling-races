import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISubscriptionType } from 'app/shared/model/subscription-type.model';
import { getEntities as getSubscriptionTypes } from 'app/entities/subscription-type/subscription-type.reducer';
import { getEntity, updateEntity, createEntity, reset } from './subscription-discount.reducer';
import { ISubscriptionDiscount } from 'app/shared/model/subscription-discount.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionDiscountUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISubscriptionDiscountUpdateState {
  isNew: boolean;
  subscriptionTypeId: string;
}

export class SubscriptionDiscountUpdate extends React.Component<ISubscriptionDiscountUpdateProps, ISubscriptionDiscountUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      subscriptionTypeId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getSubscriptionTypes();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { subscriptionDiscountEntity } = this.props;
      const entity = {
        ...subscriptionDiscountEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/subscription-discount');
  };

  render() {
    const { subscriptionDiscountEntity, subscriptionTypes, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.subscriptionDiscount.home.createOrEditLabel">Create or edit a SubscriptionDiscount</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : subscriptionDiscountEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="subscription-discount-id">ID</Label>
                    <AvInput id="subscription-discount-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="subscription-discount-name">
                    Name
                  </Label>
                  <AvField
                    id="subscription-discount-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="discountLabel" for="subscription-discount-discount">
                    Discount
                  </Label>
                  <AvField id="subscription-discount-discount" type="string" className="form-control" name="discount" />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="subscription-discount-type">
                    Type
                  </Label>
                  <AvInput
                    id="subscription-discount-type"
                    type="select"
                    className="form-control"
                    name="type"
                    value={(!isNew && subscriptionDiscountEntity.type) || 'PERCENT'}
                  >
                    <option value="PERCENT">PERCENT</option>
                    <option value="AMOUNT">AMOUNT</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="expirationDateLabel" for="subscription-discount-expirationDate">
                    Expiration Date
                  </Label>
                  <AvField id="subscription-discount-expirationDate" type="date" className="form-control" name="expirationDate" />
                </AvGroup>
                <AvGroup>
                  <Label for="subscription-discount-subscriptionType">Subscription Type</Label>
                  <AvInput id="subscription-discount-subscriptionType" type="select" className="form-control" name="subscriptionTypeId">
                    <option value="" key="0" />
                    {subscriptionTypes
                      ? subscriptionTypes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/subscription-discount" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  subscriptionTypes: storeState.subscriptionType.entities,
  subscriptionDiscountEntity: storeState.subscriptionDiscount.entity,
  loading: storeState.subscriptionDiscount.loading,
  updating: storeState.subscriptionDiscount.updating,
  updateSuccess: storeState.subscriptionDiscount.updateSuccess
});

const mapDispatchToProps = {
  getSubscriptionTypes,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SubscriptionDiscountUpdate);
