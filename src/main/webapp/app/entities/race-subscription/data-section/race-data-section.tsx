import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import PaymentMethodType from '../component/payment-method-type';
import SubscriptionType from '../component/subscription-type';
import { Box } from '@material-ui/core';
import race from 'app/entities/race/race';

interface IRaceDataSection {
  entity: any;
  race: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  cancelUrl: string;
  subscriptionTypes: any;
}

const formatter = new Intl.NumberFormat('it-IT', {
  style: 'currency',
  currency: 'EUR'
});

const raceDataSection = (props: IRaceDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Tipologia di Iscrizione</h4>
      <Row>
        <Col>
          <AvRadioGroup name="subcriptionTypeId" required errorMessage="Devi selezionare la tipologia di iscrizione">
            <Row>
              {props.subscriptionTypes.map((item, index) => (
                <Col md="4">
                  <SubscriptionType key={index} id={item.id} name={item.name} description={item.description} price={item.price} />
                </Col>
              ))}
            </Row>
          </AvRadioGroup>
        </Col>
      </Row>
      <h4 className="sheet-title">Metodo di Pagamento</h4>
      <Row>
        <Col>
          <AvGroup>
            <AvRadioGroup name="paymentType" required errorMessage="Pick one!">
              <div className="payment-method-radio-box">
                <Row>
                  {/*
                  <Col md="4">
                    <PaymentMethodType
                      label="Paypal"
                      value="PAYPAL"
                      imgUrl="content/images/paypal.png"
                    />
                  </Col>
                  */}
                  <Col md="4">
                    <PaymentMethodType label="Bonifico Bancario" value="CREDIT_TRANSFER" imgUrl="content/images/credit-transfer.png" />
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <h4 className="sheet-title">Coordinate del pagamento</h4>
                    <span>
                      Per finalizzare l'iscrizione alla gara sarà necessario effettuare un bonifico bancario utilizzando i seguenti dati:
                      <ul>
                        <li>
                          <b>IBAN:</b> IT87I9890988767578764797
                        </li>
                        <li>
                          <b>Intestatario:</b> Acsi a.s.d. Ciclismo Lazio
                        </li>
                        <li>
                          <b>Banca:</b> BNL
                        </li>
                        <li>
                          <b>Casuale:</b> Quota iscrizione gara {props.race.date}-{props.race.name}{' '}
                        </li>
                      </ul>
                    </span>
                  </Col>
                </Row>
              </div>
            </AvRadioGroup>
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

export default raceDataSection;

/*
<AvGroup>
<Label id="subcriptionTypeIdLabel" for="race-subscription-subcriptionTypeId">
  Tipologia di Iscrizione
</Label>
<AvField
  id="race-subscription-subcriptionTypeId"
  type="string"
  className="form-control"
  name="subcriptionTypeId"
  validate={{
    required: { value: true, errorMessage: 'Il campo è obbligatorio' },
    number: { value: true, errorMessage: 'This field should be a number.' }
  }}
/>
</AvGroup>

        <Col>
          <AvGroup>
            <Label id="pathTypeLabel" for="race-subscription-pathType">
              Percorso
            </Label>
            <AvField
              id="race-subscription-pathType"
              type="string"
              className="form-control"
              name="pathType"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                number: { value: true, errorMessage: 'This field should be a number.' }
              }}
            />
          </AvGroup>
        </Col>
*/
