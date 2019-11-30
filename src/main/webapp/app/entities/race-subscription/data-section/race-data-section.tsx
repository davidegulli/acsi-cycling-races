import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import { Box } from '@material-ui/core';

interface IRaceDataSection {
  entity: any;
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
            {props.subscriptionTypes.map((item, index) => (
              <Box className="box box-data" key={index}>
                <Row>
                  <Col sm="1">
                    <Box display="flex" alignItems="center" css={{ height: '100%' }}>
                      <Box>
                        <AvRadio label="" value={item.id} />
                      </Box>
                    </Box>
                  </Col>
                  <Col sm="9">
                    <Box fontWeight={500} fontSize="0.95rem">
                      {item.name}
                    </Box>
                    <Box>{item.description}</Box>
                  </Col>
                  <Col sm="2">
                    <Box display="flex" alignItems="center" justifyContent="flex-end" css={{ height: '100%' }}>
                      <Box fontWeight={500} fontSize="1.0rem" textAlign="center">
                        {formatter.format(item.price)}
                      </Box>
                    </Box>
                  </Col>
                </Row>
              </Box>
            ))}
          </AvRadioGroup>
        </Col>
      </Row>
      <h4 className="sheet-title">Metodo di Pagamento</h4>
      <Row>
        <AvGroup>
          <AvRadioGroup name="paymentType" required errorMessage="Pick one!">
            <div className="payment-method-radio-box">
              <Row>
                <Col>
                  <div className="payment-method-radio-section">
                    <Row>
                      <Col>
                        <div className="payment-method-radio">
                          <AvRadio label="Paypal" value="PAYPAL" />
                        </div>
                      </Col>
                      <Col>
                        <img src="content/images/paypal.png" className="payment-method-image" />
                      </Col>
                    </Row>
                  </div>
                </Col>
                <Col>
                  <div className="payment-method-radio-section">
                    <Row>
                      <Col>
                        <div className="payment-method-radio">
                          <AvRadio label="Bonifico Bancario" value="CREDIT_TRANSFER" />
                        </div>
                      </Col>
                      <Col>
                        <img src="content/images/credit-transfer.png" className="payment-method-image" />
                      </Col>
                    </Row>
                  </div>
                </Col>
              </Row>
            </div>
          </AvRadioGroup>
        </AvGroup>
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
