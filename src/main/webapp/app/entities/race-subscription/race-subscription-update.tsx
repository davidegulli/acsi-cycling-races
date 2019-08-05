import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRace } from 'app/shared/model/race.model';
import { getEntities as getRaces } from 'app/entities/race/race.reducer';
import { getEntity, updateEntity, createEntity, reset } from './race-subscription.reducer';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRaceSubscriptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRaceSubscriptionUpdateState {
  isNew: boolean;
  raceId: string;
  raceId: string;
}

export class RaceSubscriptionUpdate extends React.Component<IRaceSubscriptionUpdateProps, IRaceSubscriptionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      raceId: '0',
      raceId: '0',
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

    this.props.getRaces();
  }

  saveEntity = (event, errors, values) => {
    values.date = convertDateTimeToServer(values.date);

    if (errors.length === 0) {
      const { raceSubscriptionEntity } = this.props;
      const entity = {
        ...raceSubscriptionEntity,
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
    this.props.history.push('/entity/race-subscription');
  };

  render() {
    const { raceSubscriptionEntity, races, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.raceSubscription.home.createOrEditLabel">Create or edit a RaceSubscription</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : raceSubscriptionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="race-subscription-id">ID</Label>
                    <AvInput id="race-subscription-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="race-subscription-name">
                    Name
                  </Label>
                  <AvField
                    id="race-subscription-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="surnameLabel" for="race-subscription-surname">
                    Surname
                  </Label>
                  <AvField
                    id="race-subscription-surname"
                    type="text"
                    name="surname"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="birthDateLabel" for="race-subscription-birthDate">
                    Birth Date
                  </Label>
                  <AvField
                    id="race-subscription-birthDate"
                    type="text"
                    name="birthDate"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="birthPlaceLabel" for="race-subscription-birthPlace">
                    Birth Place
                  </Label>
                  <AvField
                    id="race-subscription-birthPlace"
                    type="text"
                    name="birthPlace"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel" for="race-subscription-gender">
                    Gender
                  </Label>
                  <AvInput
                    id="race-subscription-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && raceSubscriptionEntity.gender) || 'MALE'}
                  >
                    <option value="MALE">MALE</option>
                    <option value="FEMALE">FEMALE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="taxCodeLabel" for="race-subscription-taxCode">
                    Tax Code
                  </Label>
                  <AvField
                    id="race-subscription-taxCode"
                    type="text"
                    name="taxCode"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="race-subscription-email">
                    Email
                  </Label>
                  <AvField
                    id="race-subscription-email"
                    type="text"
                    name="email"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="phoneLabel" for="race-subscription-phone">
                    Phone
                  </Label>
                  <AvField
                    id="race-subscription-phone"
                    type="text"
                    name="phone"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="categoryLabel" for="race-subscription-category">
                    Category
                  </Label>
                  <AvField id="race-subscription-category" type="text" name="category" />
                </AvGroup>
                <AvGroup>
                  <Label id="subcriptionTypeIdLabel" for="race-subscription-subcriptionTypeId">
                    Subcription Type Id
                  </Label>
                  <AvField
                    id="race-subscription-subcriptionTypeId"
                    type="string"
                    className="form-control"
                    name="subcriptionTypeId"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="pathTypeLabel" for="race-subscription-pathType">
                    Path Type
                  </Label>
                  <AvField
                    id="race-subscription-pathType"
                    type="string"
                    className="form-control"
                    name="pathType"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="teamIdLabel" for="race-subscription-teamId">
                    Team Id
                  </Label>
                  <AvField
                    id="race-subscription-teamId"
                    type="string"
                    className="form-control"
                    name="teamId"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="athleteIdLabel" for="race-subscription-athleteId">
                    Athlete Id
                  </Label>
                  <AvField
                    id="race-subscription-athleteId"
                    type="text"
                    name="athleteId"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dateLabel" for="race-subscription-date">
                    Date
                  </Label>
                  <AvInput
                    id="race-subscription-date"
                    type="datetime-local"
                    className="form-control"
                    name="date"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.raceSubscriptionEntity.date)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentTypeLabel" for="race-subscription-paymentType">
                    Payment Type
                  </Label>
                  <AvInput
                    id="race-subscription-paymentType"
                    type="select"
                    className="form-control"
                    name="paymentType"
                    value={(!isNew && raceSubscriptionEntity.paymentType) || 'PAYPAL'}
                  >
                    <option value="PAYPAL">PAYPAL</option>
                    <option value="CREDIT_TRANSFER">CREDIT_TRANSFER</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="payedLabel" check>
                    <AvInput id="race-subscription-payed" type="checkbox" className="form-control" name="payed" />
                    Payed
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="payedPriceLabel" for="race-subscription-payedPrice">
                    Payed Price
                  </Label>
                  <AvField id="race-subscription-payedPrice" type="string" className="form-control" name="payedPrice" />
                </AvGroup>
                <AvGroup>
                  <Label for="race-subscription-race">Race</Label>
                  <AvInput id="race-subscription-race" type="select" className="form-control" name="raceId">
                    <option value="" key="0" />
                    {races
                      ? races.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="race-subscription-race">Race</Label>
                  <AvInput id="race-subscription-race" type="select" className="form-control" name="raceId">
                    <option value="" key="0" />
                    {races
                      ? races.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/race-subscription" replace color="info">
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
  races: storeState.race.entities,
  raceSubscriptionEntity: storeState.raceSubscription.entity,
  loading: storeState.raceSubscription.loading,
  updating: storeState.raceSubscription.updating,
  updateSuccess: storeState.raceSubscription.updateSuccess
});

const mapDispatchToProps = {
  getRaces,
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
)(RaceSubscriptionUpdate);
