import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';

interface ITeamDataSection {
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

const teamDataSection = (props: ITeamDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Associazione di Appartenza</h4>
      <Row>
        <Col>
          <AvInput name="teamId" type="hidden" />
          <AvGroup>
            <Label id="teamCodeLabel" for="race-subscription-teamCode">
              Codice Associazione
            </Label>
            <AvField
              id="race-subscription-teamCode"
              type="string"
              className="form-control"
              name="teamCode"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
          </AvGroup>
        </Col>
        <Col>
          <AvGroup>
            <Label id="teamNameLabel" for="race-subscription-teamName">
              Nome Associazione
            </Label>
            <AvField
              id="race-subscription-teamName"
              type="string"
              className="form-control"
              name="teamName"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
          </AvGroup>
        </Col>
      </Row>
      <Row>
        <Col>
          <AvGroup>
            <Label id="athleteIdLabel" for="race-subscription-athleteId">
              Numero di Tesseramento
            </Label>
            <AvField
              id="race-subscription-athleteId"
              type="text"
              name="athleteId"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
          </AvGroup>
        </Col>
        <Col>
          <AvGroup>
            <Label id="categoryLabel" for="race-subscription-category">
              Categoria
            </Label>
            <AvField id="race-subscription-category" type="text" name="category" />
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

export default teamDataSection;
