import React from 'react';
import { Button } from 'reactstrap';
import { Link } from 'react-router-dom';

interface IStepperButtons {
  activeStep: number;
  stepsLength: number;
  prevStepHandler: any;
  updating: boolean;
  cancelUrl: string;
}

const stepperButtons = (props: IStepperButtons) => (
  <div className="form-button-holder">
    <Button tag={Link} id="cancel-save" to={props.cancelUrl} replace>
      <span className="d-none d-md-inline">Annulla</span>
    </Button>
    {props.activeStep > 0 ? (
      <Button id="save-entity" disabled={props.updating} onClick={props.prevStepHandler} className="ml-2">
        Indierto
      </Button>
    ) : null}
    <Button color="primary" id="save-entity" type="submit" disabled={props.updating} className="ml-2">
      Procedi
    </Button>
  </div>
);

export default stepperButtons;
