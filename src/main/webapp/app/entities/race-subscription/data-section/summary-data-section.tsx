import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import { Box } from '@material-ui/core';

interface IPaymentDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  results: any;
}

const paymentDataSection = (props: IPaymentDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <Box>Iscrizione effettuata con success</Box>
  </div>
);

export default paymentDataSection;
