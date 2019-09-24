import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';

interface IRaceDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  raceTypes: any;
  cancelUrl: string;
}

const raceDataSection = (props: IRaceDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Informazioni Gara</h4>
      <AvGroup>
        <Label id="dateLabel" for="race-date">
          Data
        </Label>
        <AvField
          id="race-date"
          type="date"
          className="form-control"
          name="date"
          placeholder={'GG-MM-AAAA'}
          validate={{
            required: { value: true, errorMessage: 'Il campo è obbligatorio' }
          }}
        />
      </AvGroup>
      <AvGroup>
        <Label id="locationLabel" for="race-location">
          Luogo
        </Label>
        <AvField
          id="race-location"
          type="text"
          name="location"
          validate={{
            required: { value: true, errorMessage: 'Il campo è obbligatorio' }
          }}
        />
      </AvGroup>
      <AvGroup>
        <Label id="addressLabel" for="race-address">
          Indirizzo
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
        <Label id="infoLabel" for="race-info">
          Informazioni Utili
        </Label>
        <AvField id="race-info" type="textarea" name="info" />
      </AvGroup>
      <AvGroup>
        <Label id="rulesLabel" for="race-rules">
          Regolamento
        </Label>
        <AvField id="race-rules" type="textarea" name="rules" />
      </AvGroup>
      <AvGroup>
        <Label id="subscriptionExpirationDateLabel" for="race-subscriptionExpirationDate">
          Data Chiusura Iscrizioni
        </Label>
        <AvField
          id="race-subscriptionExpirationDate"
          type="date"
          className="form-control"
          name="subscriptionExpirationDate"
          placeholder={'GG-MM-AAAA'}
          validate={{
            required: { value: true, errorMessage: 'Il campo è obbligatorio' }
          }}
        />
      </AvGroup>
      <AvGroup>
        <Label for="race-type">Disciplina</Label>
        <AvField id="race-type" type="select" className="form-control" name="typeId">
          <option value="" key="0" />
          {props.raceTypes
            ? props.raceTypes.map(otherEntity => (
                <option value={otherEntity.id} key={otherEntity.id}>
                  {otherEntity.name}
                </option>
              ))
            : null}
        </AvField>
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

export default raceDataSection;

//value={props.isNew ? null : convertDateTimeFromServer(props.entity.subscriptionExpirationDate)}
