import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import SubscriptionType from './subscription-type';
import PathType from './path-type';

interface ISubscriptionDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  subscriptionTypeRows: any;
  addSubscriptionTypeHandler: any;
  removeSubscriptionTypeHandler: any;
  pathTypeRows: any;
  addPathTypeHandler: any;
  removePathTypeHandler: any;
  cancelUrl: string;
}

const subscriptionDataSection = (props: ISubscriptionDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Informazioni Iscrizione</h4>
      <SubscriptionType
        rows={props.subscriptionTypeRows}
        addRowHandler={props.addSubscriptionTypeHandler}
        removeRowHandler={props.removeSubscriptionTypeHandler}
      />
      <PathType rows={props.pathTypeRows} addRowHandler={props.addPathTypeHandler} removeRowHandler={props.removePathTypeHandler} />
      <StepperButtons
        activeStep={props.activeStep}
        stepsLength={props.stepsLength}
        updating={props.updating}
        prevStepHandler={props.prevStepHandler}
        cancelUrl={props.cancelUrl}
      />
    </AvForm>
  </div>
);

export default subscriptionDataSection;
