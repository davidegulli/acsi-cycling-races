import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
import { getEntities as getAcsiTeams } from 'app/entities/acsi-team/acsi-team.reducer';
import { getEntity, updateEntity, createEntity, reset } from './race.reducer';
import { IRace } from 'app/shared/model/race.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRaceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRaceUpdateState {
  isNew: boolean;
  acsiTeamId: string;
}

export class RaceUpdate extends React.Component<IRaceUpdateProps, IRaceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      acsiTeamId: '0',
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

    this.props.getAcsiTeams();
  }

  saveEntity = (event, errors, values) => {
    values.date = convertDateTimeToServer(values.date);
    values.subscriptionExpirationDate = convertDateTimeToServer(values.subscriptionExpirationDate);

    if (errors.length === 0) {
      const { raceEntity } = this.props;
      const entity = {
        ...raceEntity,
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
    this.props.history.push('/entity/race');
  };

  render() {
    const { raceEntity, acsiTeams, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.race.home.createOrEditLabel">Create or edit a Race</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : raceEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="race-id">ID</Label>
                    <AvInput id="race-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="race-name">
                    Name
                  </Label>
                  <AvField
                    id="race-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dateLabel" for="race-date">
                    Date
                  </Label>
                  <AvInput
                    id="race-date"
                    type="datetime-local"
                    className="form-control"
                    name="date"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.raceEntity.date)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="locationLabel" for="race-location">
                    Location
                  </Label>
                  <AvField
                    id="race-location"
                    type="text"
                    name="location"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="race-description">
                    Description
                  </Label>
                  <AvField id="race-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="infoLabel" for="race-info">
                    Info
                  </Label>
                  <AvField id="race-info" type="text" name="info" />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="race-address">
                    Address
                  </Label>
                  <AvField
                    id="race-address"
                    type="text"
                    name="address"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="latitudeLabel" for="race-latitude">
                    Latitude
                  </Label>
                  <AvField id="race-latitude" type="string" className="form-control" name="latitude" />
                </AvGroup>
                <AvGroup>
                  <Label id="longitudeLabel" for="race-longitude">
                    Longitude
                  </Label>
                  <AvField id="race-longitude" type="string" className="form-control" name="longitude" />
                </AvGroup>
                <AvGroup>
                  <Label id="rulesLabel" for="race-rules">
                    Rules
                  </Label>
                  <AvField id="race-rules" type="text" name="rules" />
                </AvGroup>
                <AvGroup>
                  <Label id="subscriptionExpirationDateLabel" for="race-subscriptionExpirationDate">
                    Subscription Expiration Date
                  </Label>
                  <AvInput
                    id="race-subscriptionExpirationDate"
                    type="datetime-local"
                    className="form-control"
                    name="subscriptionExpirationDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.raceEntity.subscriptionExpirationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="attributesLabel" for="race-attributes">
                    Attributes
                  </Label>
                  <AvField id="race-attributes" type="text" name="attributes" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="race-status">
                    Status
                  </Label>
                  <AvInput
                    id="race-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && raceEntity.status) || 'PUBLISHED'}
                  >
                    <option value="PUBLISHED">PUBLISHED</option>
                    <option value="DRAFT">DRAFT</option>
                    <option value="CANCELED">CANCELED</option>
                    <option value="UNPUBLISHED">UNPUBLISHED</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="race-acsiTeam">Acsi Team</Label>
                  <AvInput id="race-acsiTeam" type="select" className="form-control" name="acsiTeamId">
                    <option value="" key="0" />
                    {acsiTeams
                      ? acsiTeams.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/race" replace color="info">
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
  acsiTeams: storeState.acsiTeam.entities,
  raceEntity: storeState.race.entity,
  loading: storeState.race.loading,
  updating: storeState.race.updating,
  updateSuccess: storeState.race.updateSuccess
});

const mapDispatchToProps = {
  getAcsiTeams,
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
)(RaceUpdate);
