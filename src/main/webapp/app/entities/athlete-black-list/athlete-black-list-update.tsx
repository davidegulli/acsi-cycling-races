import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './athlete-black-list.reducer';
import { IAthleteBlackList } from 'app/shared/model/athlete-black-list.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAthleteBlackListUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAthleteBlackListUpdateState {
  isNew: boolean;
}

export class AthleteBlackListUpdate extends React.Component<IAthleteBlackListUpdateProps, IAthleteBlackListUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    values.birthDate = convertDateTimeToServer(values.birthDate);

    if (errors.length === 0) {
      const { athleteBlackListEntity } = this.props;
      const entity = {
        ...athleteBlackListEntity,
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
    this.props.history.push('/entity/athlete-black-list');
  };

  render() {
    const { athleteBlackListEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.athleteBlackList.home.createOrEditLabel">Create or edit a AthleteBlackList</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : athleteBlackListEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="athlete-black-list-id">ID</Label>
                    <AvInput id="athlete-black-list-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="athlete-black-list-name">
                    Name
                  </Label>
                  <AvField
                    id="athlete-black-list-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="surnameLabel" for="athlete-black-list-surname">
                    Surname
                  </Label>
                  <AvField
                    id="athlete-black-list-surname"
                    type="text"
                    name="surname"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="birthDateLabel" for="athlete-black-list-birthDate">
                    Birth Date
                  </Label>
                  <AvInput
                    id="athlete-black-list-birthDate"
                    type="datetime-local"
                    className="form-control"
                    name="birthDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.athleteBlackListEntity.birthDate)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="taxCodeLabel" for="athlete-black-list-taxCode">
                    Tax Code
                  </Label>
                  <AvField
                    id="athlete-black-list-taxCode"
                    type="text"
                    name="taxCode"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/athlete-black-list" replace color="info">
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
  athleteBlackListEntity: storeState.athleteBlackList.entity,
  loading: storeState.athleteBlackList.loading,
  updating: storeState.athleteBlackList.updating,
  updateSuccess: storeState.athleteBlackList.updateSuccess
});

const mapDispatchToProps = {
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
)(AthleteBlackListUpdate);
