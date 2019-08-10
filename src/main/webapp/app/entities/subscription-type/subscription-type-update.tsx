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
import { getEntity, updateEntity, createEntity, reset } from './subscription-type.reducer';
import { ISubscriptionType } from 'app/shared/model/subscription-type.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISubscriptionTypeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISubscriptionTypeUpdateState {
  isNew: boolean;
  raceId: string;
}

export class SubscriptionTypeUpdate extends React.Component<ISubscriptionTypeUpdateProps, ISubscriptionTypeUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
    if (errors.length === 0) {
      const { subscriptionTypeEntity } = this.props;
      const entity = {
        ...subscriptionTypeEntity,
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
    this.props.history.push('/entity/subscription-type');
  };

  render() {
    const { subscriptionTypeEntity, races, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.subscriptionType.home.createOrEditLabel">Create or edit a SubscriptionType</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : subscriptionTypeEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="subscription-type-id">ID</Label>
                    <AvInput id="subscription-type-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="subscription-type-name">
                    Name
                  </Label>
                  <AvField
                    id="subscription-type-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="subscription-type-description">
                    Description
                  </Label>
                  <AvField
                    id="subscription-type-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="rulesLabel" for="subscription-type-rules">
                    Rules
                  </Label>
                  <AvField id="subscription-type-rules" type="text" name="rules" />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="subscription-type-price">
                    Price
                  </Label>
                  <AvField id="subscription-type-price" type="string" className="form-control" name="price" />
                </AvGroup>
                <AvGroup>
                  <Label for="subscription-type-race">Race</Label>
                  <AvInput id="subscription-type-race" type="select" className="form-control" name="raceId">
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
                <Button tag={Link} id="cancel-save" to="/entity/subscription-type" replace color="info">
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
  subscriptionTypeEntity: storeState.subscriptionType.entity,
  loading: storeState.subscriptionType.loading,
  updating: storeState.subscriptionType.updating,
  updateSuccess: storeState.subscriptionType.updateSuccess
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
)(SubscriptionTypeUpdate);
