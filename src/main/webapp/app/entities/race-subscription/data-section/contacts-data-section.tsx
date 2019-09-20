import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';

interface IContactsDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  cancelUrl: string;
}

const contactsDataSection = (props: IContactsDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Contatti</h4>
      <Row>
        <Col>
          <AvGroup>
            <Label id="emailLabel" for="race-subscription-email">
              Email
            </Label>
            <AvField
              id="race-subscription-email"
              type="email"
              name="email"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                email: { value: true, errorMessage: 'La mail immessa non è corretta' }
              }}
            />
          </AvGroup>
        </Col>
        <Col>
          <AvGroup>
            <Label id="phoneLabel" for="race-subscription-phone">
              Telefono
            </Label>
            <AvField
              id="race-subscription-phone"
              type="text"
              name="phone"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
          </AvGroup>
        </Col>
      </Row>
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

export default contactsDataSection;
