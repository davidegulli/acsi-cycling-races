import React, { useRef } from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import _debounce from 'lodash.debounce';
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import moment from 'moment';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

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

const raceDataSection = (props: IRaceDataSection) => {
  let { date } = props.entity;

  const dateChangeHandler = event => {
    date = event.target.value;
  };

  const validateExpirationDate = _debounce((value, ctx, input, cb) => {
    if (date == null || date === '') {
      cb('Per validate la data di scadenza delle iscrizioni è necessario inserire la data di svolgimento della gara');
    }

    const dateMoment = moment(date, 'YYYY-MM-DD');
    const datePlusThresholdMoment = dateMoment.subtract(30, 'days');
    const expirationDateMoment = moment(value, 'YYYY-MM-DD');

    if (expirationDateMoment.isBefore(datePlusThresholdMoment)) {
      cb('La data di scadenza delle iscrizioni deve essere impostata almeno 30 giorni prima la data di svolgimento della gara');
    }

    cb(true);
  }, 300);

  return (
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
            onChange={dateChangeHandler}
            validate={{
              required: { value: true, errorMessage: 'Il campo è obbligatorio' },
              dateRange: {
                format: 'MM/DD/YYYY',
                start: { value: 1, units: 'days' },
                end: { value: 2, units: 'years' },
                errorMessage: 'La data inserita non è validata, deve essere una data futura'
              }
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
              required: { value: true, errorMessage: 'Il campo è obbligatorio' },
              pattern: { value: "^[A-Za-z'àèùì ]+$", errorMessage: 'Il campo può contenere solamente caratteri alfabetici' },
              minLength: { value: 4, errorMessage: 'Il campo deve esse composto da almeno 4 caratteri' },
              maxLength: { value: 30, errorMessage: 'Il campo può essere composto da un massimo di 30 caratteri' }
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
              required: { value: true, errorMessage: 'This field is required.' },
              pattern: { value: "^[A-Za-z0-9',àèùì ]+$", errorMessage: 'Il campo può contenere solamente caratteri alfabetici e numerici' },
              minLength: { value: 4, errorMessage: 'Il campo deve esse composto da almeno 4 caratteri' },
              maxLength: { value: 30, errorMessage: 'Il campo può essere composto da un massimo di 30 caratteri' }
            }}
          />
        </AvGroup>
        <AvGroup>
          <Label id="infoLabel" for="race-info">
            Informazioni Utili
          </Label>
          <AvField
            id="race-info"
            type="textarea"
            name="info"
            validate={{
              minLength: { value: 4, errorMessage: 'Il campo deve esse composto da almeno 4 caratteri' },
              maxLength: { value: 200, errorMessage: 'Il campo può essere composto da un massimo di 200 caratteri' }
            }}
          />
        </AvGroup>
        <AvGroup>
          <Label id="rulesLabel" for="race-rules">
            Regolamento
          </Label>
          <AvField
            id="race-rules"
            type="textarea"
            name="rules"
            validate={{
              minLength: { value: 4, errorMessage: 'Il campo deve esse composto da almeno 4 caratteri' },
              maxLength: { value: 200, errorMessage: 'Il campo può essere composto da un massimo di 200 caratteri' }
            }}
          />
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
              /*async: validateExpirationDate*/
            }}
          />
        </AvGroup>
        <AvGroup>
          <Label for="race-type">Disciplina</Label>
          <AvField
            id="race-type"
            type="select"
            className="form-control"
            name="typeId"
            validate={{
              required: { value: true, errorMessage: 'Il campo è obbligatorio' }
            }}
          >
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
};

export default raceDataSection;

// value={props.isNew ? null : convertDateTimeFromServer(props.entity.subscriptionExpirationDate)}
