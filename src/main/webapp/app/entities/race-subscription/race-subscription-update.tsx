import './race-subscription.scss';

import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRace } from 'app/shared/model/race.model';
import { getEntity as getRace } from 'app/entities/race/race.reducer';
import { getEntitiesByRace as getSubscriptionTypes } from 'app/entities/subscription-type/subscription-type.reducer';
import { getEntitiesByRace as getPathTypes } from 'app/entities/path-type/path-type.reducer';
import { getEntity, updateEntity, createEntity, reset } from './race-subscription.reducer';
import { getEntityByGenderAndBirthDate as getCategory } from 'app/entities/category/category.reducer';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import RaceHeader from '../race/race-header';
import { Stepper, Step, StepLabel, StepIcon } from '@material-ui/core';
import PersonalDataSection from './data-section/personal-data-section';
import DocumentsDataSection from './data-section/documents-data-section';
import TeamDataSection from './data-section/team-data-section';
import RaceDataSection from './data-section/race-data-section';
import PaymentDataSection from './data-section/payment-data-section';
import { withStyles } from '@material-ui/styles';

export interface IRaceSubscriptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string; raceId: string }> {}

export interface IRaceSubscriptionUpdateState {
  isNew: boolean;
  raceId: string;
  step: number;
  gender: string;
  birthDate: string;
  values: any;
  errors: any;
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
  return ['Dati Personali', 'Documenti', 'Associazione', 'Gara', 'Pagamento'];
}

export class RaceSubscriptionUpdate extends React.Component<IRaceSubscriptionUpdateProps, IRaceSubscriptionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      raceId: this.props.match.params.raceId,
      isNew: !this.props.match.params || !this.props.match.params.id,
      step: 0,
      gender: 'MALE',
      birthDate: '',
      values: {},
      errors: []
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

    this.props.getRace(this.props.match.params.raceId);
    this.props.getSubscriptionTypes(this.props.match.params.raceId);
    this.props.getPathTypes(this.props.match.params.raceId);
  }

  saveEntity = (errors, values) => {
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

  nextStepHandler = (event, errors, values) => {
    if (errors.length === 0) {
      const currentErrors = this.state.errors;
      const updateErrors = [...currentErrors, ...errors];

      const currentValues = this.state.values;
      const updatedValues = { ...currentValues, ...values };
      this.setState({ values: updatedValues, errors: updateErrors });

      if (this.state.step === 4) {
        this.saveEntity(updateErrors, updatedValues);
      } else {
        const currentState = this.state.step;
        this.setState({ step: currentState + 1 });
      }
    }
  };

  onDropPersonalIdDoc = () => {};

  onDropMedicalCertificationDoc = () => {};

  onChangeGenderHandler = event => {
    const gender = event.target.value;
    const { birthDate } = this.state;
    this.setState({ gender });
    this.setCategory(gender, birthDate);
  };

  onChangeBirthDateHandler = event => {
    const birthDate = event.target.value;
    const { gender } = this.state;
    this.setState({ birthDate });
    this.setCategory(gender, birthDate);
  };

  setCategory = (gender, birthDate) => {
    if (gender !== null && gender !== undefined && gender !== '' && birthDate !== null && birthDate !== undefined && birthDate !== '') {
      this.props.getCategory(gender, birthDate);
    }
  };

  prevStepHandler = () => {
    const currentState = this.state.step;
    this.setState({ step: currentState - 1 });
  };

  render() {
    const { raceSubscriptionEntity, subscriptionTypes, pathTypes, race, loading, updating } = this.props;
    const { step } = this.state;
    const steps = getSteps();
    const cancelUrl = '/event/' + this.state.raceId;

    return (
      <div>
        <RaceHeader entity={race} showButton={false} />
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
                <PersonalDataSection
                  activeStep={this.state.step}
                  stepIndex={0}
                  stepsLength={5}
                  entity={raceSubscriptionEntity}
                  isNew={this.state.isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  raceId={race.id}
                  cancelUrl={cancelUrl}
                  onChangeGenderHandler={this.onChangeGenderHandler}
                  onChangeBirthDateHandler={this.onChangeBirthDateHandler}
                />
                <DocumentsDataSection
                  activeStep={this.state.step}
                  stepIndex={1}
                  stepsLength={5}
                  entity={raceSubscriptionEntity}
                  isNew={this.state.isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  cancelUrl={cancelUrl}
                  onDropPersonalIdDoc={this.onDropPersonalIdDoc}
                  onDropMedicalCertificationDoc={this.onDropMedicalCertificationDoc}
                />
                <TeamDataSection
                  activeStep={this.state.step}
                  stepIndex={2}
                  stepsLength={5}
                  entity={raceSubscriptionEntity}
                  isNew={this.state.isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  cancelUrl={cancelUrl}
                  category={this.props.category}
                />
                <RaceDataSection
                  activeStep={this.state.step}
                  stepIndex={3}
                  stepsLength={5}
                  entity={raceSubscriptionEntity}
                  isNew={this.state.isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
                  subscriptionTypes={subscriptionTypes}
                  pathTypes={pathTypes}
                  cancelUrl={cancelUrl}
                />
                <PaymentDataSection
                  activeStep={this.state.step}
                  stepIndex={4}
                  stepsLength={5}
                  entity={raceSubscriptionEntity}
                  isNew={this.state.isNew}
                  updating={updating}
                  nextStepHandler={this.nextStepHandler}
                  prevStepHandler={this.prevStepHandler}
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
  race: storeState.race.entity,
  subscriptionTypes: storeState.subscriptionType.entities,
  pathTypes: storeState.pathType.entities,
  raceSubscriptionEntity: storeState.raceSubscription.entity,
  category: storeState.category.entity,
  loading: storeState.raceSubscription.loading,
  updating: storeState.raceSubscription.updating,
  updateSuccess: storeState.raceSubscription.updateSuccess
});

const mapDispatchToProps = {
  getRace,
  getSubscriptionTypes,
  getPathTypes,
  getEntity,
  getCategory,
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
