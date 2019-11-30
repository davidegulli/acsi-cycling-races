import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';

interface IPaymentDataSection {
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

const paymentDataSection = (props: IPaymentDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Metodo di Pagamento</h4>
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

export default paymentDataSection;
