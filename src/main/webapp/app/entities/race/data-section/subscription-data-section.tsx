import React, { useState } from 'react';
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

const subscriptionDataSection = (props: ISubscriptionDataSection) => {
  const subscriptionTypeRows = props.subscriptionTypeRows;
  const pathTypeRows = props.pathTypeRows;
  const [formConfirmed, setFormConfirmed] = useState(false);
  const [subscriptionTypeValid, setSubscriptionTypeValid] = useState(true);
  const [pathTypeValid, setPathTypeValid] = useState(true);

  const formSubmitHandler = (event, errors, values) => {
    setFormConfirmed(true);

    if (validate()) {
      props.nextStepHandler(event, errors, values);
    }
  };

  const validate = () => {
    let tmpSubscriptionTypeValid = true;
    let tmpPathTypeValid = true;

    if (subscriptionTypeRows.length <= 0) {
      tmpSubscriptionTypeValid = false;
    }

    if (pathTypeRows.length <= 0) {
      tmpPathTypeValid = false;
    }

    setSubscriptionTypeValid(tmpSubscriptionTypeValid);
    setPathTypeValid(tmpPathTypeValid);

    return tmpSubscriptionTypeValid && tmpPathTypeValid;
  };

  return (
    <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
      <AvForm model={props.isNew ? {} : props.entity} onSubmit={formSubmitHandler}>
        <h4 className="sheet-title">Informazioni Iscrizione</h4>
        <SubscriptionType
          rows={props.subscriptionTypeRows}
          addRowHandler={props.addSubscriptionTypeHandler}
          removeRowHandler={props.removeSubscriptionTypeHandler}
        />
        {formConfirmed && !subscriptionTypeValid ? (
          <div className="invalid-feedback">Devi inserire almeno una tipologia di iscrizione!</div>
        ) : null}
        <PathType rows={props.pathTypeRows} addRowHandler={props.addPathTypeHandler} removeRowHandler={props.removePathTypeHandler} />
        {formConfirmed && !pathTypeValid ? <div className="invalid-feedback">Devi inserire almeno una tipologia di percorso!</div> : null}
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
};

export default subscriptionDataSection;
