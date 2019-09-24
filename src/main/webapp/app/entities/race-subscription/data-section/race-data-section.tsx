import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import { element } from 'prop-types';
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
  pathTypes: any;
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
            {props.subscriptionTypes.map((element, index) => (
              <Box className="box box-data">
                <Row>
                  <Col sm="1">
                    <Box display="flex" alignItems="center" css={{ height: '100%' }}>
                      <Box>
                        <AvRadio label="" value={element.id} />
                      </Box>
                    </Box>
                  </Col>
                  <Col sm="9">
                    <Box fontWeight={500} fontSize="0.95rem">
                      {element.name}
                    </Box>
                    <Box>{element.description}</Box>
                  </Col>
                  <Col sm="2">
                    <Box display="flex" alignItems="center" justifyContent="flex-end" css={{ height: '100%' }}>
                      <Box fontWeight={500} fontSize="1.0rem" textAlign="center">
                        {formatter.format(element.price)}
                      </Box>
                    </Box>
                  </Col>
                </Row>
              </Box>
            ))}
          </AvRadioGroup>
        </Col>
      </Row>
      <h4 className="sheet-title">Tipologia di Percorso</h4>
      <Row>
        <Col>
          <AvRadioGroup name="pathTypeId" required errorMessage="Devi selezionare la tipologia di percorso">
            {props.pathTypes.map((element, index) => (
              <Box className="box box-data">
                <Row>
                  <Col sm="1">
                    <Box display="flex" alignItems="center" css={{ height: '100%' }}>
                      <Box>
                        <AvRadio label="" value={element.id} />
                      </Box>
                    </Box>
                  </Col>
                  <Col sm="9">
                    <Box fontWeight={500} fontSize="0.95rem">
                      {element.name}
                    </Box>
                    <Box>{element.description}</Box>
                  </Col>
                  <Col sm="2">
                    <Box display="flex" alignItems="center" justifyContent="flex-end" css={{ height: '100%' }}>
                      <Box fontWeight={500} fontSize="1.0rem" textAlign="center">
                        {element.distance} Km
                      </Box>
                    </Box>
                  </Col>
                </Row>
              </Box>
            ))}
          </AvRadioGroup>
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
