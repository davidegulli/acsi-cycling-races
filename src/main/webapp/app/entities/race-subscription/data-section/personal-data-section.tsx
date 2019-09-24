import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';

interface IPersonalDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  raceId: number;
  cancelUrl: string;
  onChangeGenderHandler: any;
  onChangeBirthDateHandler: any;
}

const personalDataSection = (props: IPersonalDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <AvInput type="hidden" name="raceId" value={props.raceId} />
      <h4 className="sheet-title">Informazioni Personali</h4>
      <Row>
        <Col>
          <AvGroup>
            <Label id="nameLabel" for="race-subscription-name">
              Nome
            </Label>
            <AvField
              id="race-subscription-name"
              type="text"
              name="name"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
          </AvGroup>
        </Col>
        <Col>
          <AvGroup>
            <Label id="surnameLabel" for="race-subscription-surname">
              Cognome
            </Label>
            <AvField
              id="race-subscription-surname"
              type="text"
              name="surname"
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
            <Label id="birthDateLabel" for="race-subscription-birthDate">
              Data di Nascita
            </Label>
            <AvField
              id="race-subscription-birthDate"
              type="date"
              name="birthDate"
              placeholder={'GG-MM-AAAA'}
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
              onChange={props.onChangeBirthDateHandler}
            />
          </AvGroup>
        </Col>
        <Col>
          <AvGroup>
            <Label id="birthPlaceLabel" for="race-subscription-birthPlace">
              Luogo di Nascita
            </Label>
            <AvField
              id="race-subscription-birthPlace"
              type="text"
              name="birthPlace"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
          </AvGroup>
        </Col>
      </Row>
      <Row>
        <Col md="3">
          <AvGroup>
            <Label id="genderLabel" for="race-subscription-gender">
              Sesso
            </Label>
            <AvInput
              id="race-subscription-gender"
              type="select"
              className="form-control"
              name="gender"
              value={(!props.isNew && props.entity.gender) || 'MALE'}
              onChange={props.onChangeGenderHandler}
            >
              <option value="MALE">MALE</option>
              <option value="FEMALE">FEMALE</option>
            </AvInput>
          </AvGroup>
        </Col>
        <Col md="9">
          <AvGroup>
            <Label id="taxCodeLabel" for="race-subscription-taxCode">
              Codice Fiscale
            </Label>
            <AvField
              id="race-subscription-taxCode"
              type="text"
              name="taxCode"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
          </AvGroup>
        </Col>
      </Row>
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

export default personalDataSection;
