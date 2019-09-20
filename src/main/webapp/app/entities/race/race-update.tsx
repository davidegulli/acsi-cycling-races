import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { setFileData } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRaceType } from 'app/shared/model/race-type.model';
import { getEntities as getRaceTypes } from 'app/entities/race-type/race-type.reducer';
import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
import { getEntityByUserLogged as getAcsiTeam } from 'app/entities/acsi-team/acsi-team.reducer';
// tslint:disable-next-line:no-duplicate-imports
import { getEntity, updateEntity, createEntity, setBlob } from './race.reducer';
import { addSubscriptionType, removeSubscriptionType, addPathType, removePathType, reset } from './race.reducer';
import { IRace } from 'app/shared/model/race.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import ImageUploader from '../../shared/component/image-uploader';

import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';

import GeneralDataSection from './data-section/genaral-data-section';
import RaceDataSection from './data-section/race-data-section';
import ImagesDataSection from './data-section/images-data-section';
import SubscriptionDataSection from './data-section/subscription-data-section';
import race from './race';
import { StepIcon } from '@material-ui/core';
import { withStyles } from '@material-ui/styles';

export interface IRaceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRaceUpdateState {
  isNew: boolean;
  typeId: string;
  acsiTeamId: string;
  step: number;
  values: any;
  errors: any;
  pathTypeRows: any;
}

const IconLabel = withStyles({
  active: {
    color: '#007bff !important'
  },
  completed: {
    color: '#007bff !important'
  }
})(StepIcon);

function getSteps() {
  return ['Generali', 'Gara', 'Immagini', 'Iscrizioni'];
}

export class RaceUpdate extends React.Component<IRaceUpdateProps, IRaceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      typeId: '0',
      acsiTeamId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id,
      step: 0,
      values: {},
      errors: [],
      pathTypeRows: []
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

    this.props.getRaceTypes();
    this.props.getAcsiTeam(0);
  }

  saveEntity = (errors, values) => {
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

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  onDropLogoImage = (event, acceptedFiles) => {
    setFileData(event, (contentType, data) => this.props.setBlob('binaryLogo', data, contentType), true);
  };

  onDropCoverImage = (event, acceptedFiles) => {
    setFileData(event, (contentType, data) => this.props.setBlob('binaryCover', data, contentType), true);
  };

  onDropPathMapImage = (event, acceptedFiles) => {
    setFileData(event, (contentType, data) => this.props.setBlob('binaryPathMap', data, contentType), true);
  };

  onAddSubscriptionTypeHandler = subscriptionTypeRow => {
    this.props.addSubscriptionType(subscriptionTypeRow);
  };

  onRemoveSubscriptionTypeHandler = index => {
    this.props.removeSubscriptionType(index);
  };

  onAddPathTypeHandler = pathTypeRow => {
    this.props.addPathType(pathTypeRow);
  };

  onRemovePathTypeHandler = index => {
    this.props.removePathType(index);
  };

  nextStepHandler = (event, errors, values) => {
    if (errors.length === 0) {
      const currentErrors = this.state.errors;
      const updateErrors = [...currentErrors, ...errors];

      const currentValues = this.state.values;
      const updatedValues = { ...currentValues, ...values };
      updatedValues['pathTypes'] = this.state.pathTypeRows;

      this.setState({ values: updatedValues, errors: updateErrors });

      if (this.state.step === 3) {
        this.saveEntity(updateErrors, updatedValues);
      } else {
        const currentState = this.state.step;
        this.setState({ step: currentState + 1 });
      }
    }
  };

  prevStepHandler = () => {
    const currentState = this.state.step;
    this.setState({ step: currentState - 1 });
  };

  render() {
    const { raceEntity, raceTypes, acsiTeam, loading, updating } = this.props;
    const { isNew, step } = this.state;
    const steps = getSteps();
    const cancelUrl = '/entity/race';

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.race.home.createOrEditLabel" className="sheet-title">
              Gara
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <Fragment>
                <Stepper activeStep={step} alternativeLabel>
                  {steps.map(label => (
                    <Step key={label}>
                      <StepLabel StepIconComponent={IconLabel}>{label}</StepLabel>
                    </Step>
                  ))}
                </Stepper>
                <GeneralDataSection
                  activeStep={this.state.step}
                  stepIndex={0}
                  stepsLength={4}
                  entity={raceEntity}
                  isNew={isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  acsiTeam={acsiTeam}
                  cancelUrl={cancelUrl}
                />
                <RaceDataSection
                  activeStep={this.state.step}
                  stepIndex={1}
                  stepsLength={4}
                  entity={raceEntity}
                  isNew={isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  raceTypes={raceTypes}
                  cancelUrl={cancelUrl}
                />
                <ImagesDataSection
                  activeStep={this.state.step}
                  stepIndex={2}
                  stepsLength={4}
                  entity={raceEntity}
                  isNew={isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  onDropLogoImage={this.onDropLogoImage}
                  onDropCoverImage={this.onDropCoverImage}
                  onDropPathMapImage={this.onDropPathMapImage}
                  cancelUrl={cancelUrl}
                />
                <SubscriptionDataSection
                  activeStep={this.state.step}
                  stepIndex={3}
                  stepsLength={4}
                  entity={raceEntity}
                  isNew={isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  subscriptionTypeRows={raceEntity.subscriptionTypes}
                  addSubscriptionTypeHandler={this.onAddSubscriptionTypeHandler}
                  removeSubscriptionTypeHandler={this.onRemoveSubscriptionTypeHandler}
                  pathTypeRows={raceEntity.pathTypes}
                  addPathTypeHandler={this.onAddPathTypeHandler}
                  removePathTypeHandler={this.onRemovePathTypeHandler}
                  cancelUrl={cancelUrl}
                />
              </Fragment>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  raceTypes: storeState.raceType.entities,
  acsiTeam: storeState.acsiTeam.entity,
  raceEntity: storeState.race.entity,
  loading: storeState.race.loading,
  updating: storeState.race.updating,
  updateSuccess: storeState.race.updateSuccess
});

const mapDispatchToProps = {
  getRaceTypes,
  getAcsiTeam,
  getEntity,
  updateEntity,
  createEntity,
  setBlob,
  addSubscriptionType,
  removeSubscriptionType,
  addPathType,
  removePathType,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceUpdate);
