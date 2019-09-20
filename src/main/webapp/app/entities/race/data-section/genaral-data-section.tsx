import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';

interface IGeneralDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  acsiTeam: any;
  cancelUrl: string;
}

const generalDataSection = (props: IGeneralDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <Row>
        <Col>
          <AvGroup>
            <Label for="race-acsiTeamCode">Codice Associazione</Label>
            <AvField id="race-acsiTeamCode" type="text" className="form-control" name="acsiTeamCode" readOnly value={props.acsiTeam.code} />
            <AvField id="race-acsiTeamId" type="hidden" name="acsiTeamId" value={props.acsiTeam.id} className="d-none" />
          </AvGroup>
        </Col>
        <Col>
          <AvGroup>
            <Label for="race-acsiTeamName">Nome Associazione</Label>
            <AvField id="race-acsiTeamName" type="text" className="form-control" name="acsiTeamName" readOnly value={props.acsiTeam.name} />
          </AvGroup>
        </Col>
      </Row>
      <AvGroup>
        <Label id="nameLabel" for="race-name">
          Nome
        </Label>
        <AvField
          id="race-name"
          type="text"
          name="name"
          validate={{
            required: { value: true, errorMessage: 'Il campo è obbligatorio' }
          }}
        />
      </AvGroup>
      <AvGroup>
        <Label id="descriptionLabel" for="race-description">
          Descrizione
        </Label>
        <AvField id="race-description" type="textarea" name="description" />
      </AvGroup>
      <h4 className="sheet-title">Contatti Organizzatore</h4>
      <AvGroup>
        <Label for="race-contactName">Nominativo</Label>
        <AvField
          id="race-contactName"
          type="text"
          className="form-control"
          name="contactName"
          helpMessage="Inserisci il nominativo del contatto responsabile dell'organizzazione della gara"
        />
      </AvGroup>
      <AvGroup>
        <Label for="race-contactEmail">E-Mail</Label>
        <AvField id="race-contactEmail" type="text" className="form-control" name="contactEmail" />
      </AvGroup>
      <AvGroup>
        <Label for="race-contactPhone">Telefono</Label>
        <AvField id="race-contactPhone" type="text" className="form-control" name="contactPhone" />
      </AvGroup>
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

export default generalDataSection;
