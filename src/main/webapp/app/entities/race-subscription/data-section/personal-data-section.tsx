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
                required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                pattern: { value: "^[A-Za-z'àèùìéò ]+$", errorMessage: 'Il campo può contenere solamente caratteri alfabetici' },
                minLength: { value: 3, errorMessage: 'Il campo deve esse composto da almeno 3 caratteri' },
                maxLength: { value: 20, errorMessage: 'Il campo deve esse composto da al massimo 20 caratteri' }
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
                required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                pattern: { value: "^[A-Za-z'àèùìéò ]+$", errorMessage: 'Il campo può contenere solamente caratteri alfabetici' },
                minLength: { value: 3, errorMessage: 'Il campo deve esse composto da almeno 3 caratteri' },
                maxLength: { value: 20, errorMessage: 'Il campo deve esse composto da al massimo 20 caratteri' }
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
                required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                pattern: { value: "^[A-Za-z'àèùìéò ]+$", errorMessage: 'Il campo può contenere solamente caratteri alfabetici' },
                minLength: { value: 3, errorMessage: 'Il campo deve esse composto da almeno 3 caratteri' },
                maxLength: { value: 20, errorMessage: 'Il campo deve esse composto da al massimo 20 caratteri' }
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
              style={{ textTransform: 'uppercase' }}
              id="race-subscription-taxCode"
              type="text"
              name="taxCode"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                pattern: { value: '[A-Z0-9]', errorMessage: 'Il campo deve contenere un codice fiscale valido' },
                minLength: { value: 16, errorMessage: 'Il campo deve contenere un codice fiscale valido' },
                maxLength: { value: 16, errorMessage: 'Il campo deve contenere un codice fiscale valido' }
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
                required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                pattern: { value: '^[0-9 -]+$', errorMessage: 'Il campo può contenere solamente caratteri numerici' },
                minLength: { value: 8, errorMessage: 'Il campo deve esse composto da almeno 8 caratteri' },
                maxLength: { value: 14, errorMessage: 'Il campo deve esse composto da al massimo 14 caratteri' }
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

/*
pattern: { value: '^(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$',
                           errorMessage: 'Il campo può contenere solamente caratteri numerici' }
*/
