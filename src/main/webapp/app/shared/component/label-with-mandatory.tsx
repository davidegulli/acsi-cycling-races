import React from 'react';
import { Label } from 'reactstrap';

const labelWithMandatory = props => {
  return (
    <Label id={props.id} for={props.for}>
      {props.children} <span className="mandatory-field">(Obbligatorio)</span>
    </Label>
  );
};

export default labelWithMandatory;
