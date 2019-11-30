import React, { useState } from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import FileUploader from '../../../shared/component/file-uploader';

interface IDocumentsDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  cancelUrl: string;
  onDropPersonalIdDoc: any;
  onDropMedicalCertificationDoc: any;
}

const documentsDataSection = (props: IDocumentsDataSection) => {
  const INITIAL_STATE = {
    formConfirmed: false,
    personalIdDocUploaded: !props.isNew,
    medicalCertificationDocUploaded: !props.isNew
  };

  const [state, setState] = useState(INITIAL_STATE);

  const onDropPersonalIdDoc = (event, acceptedFiles) => {
    setState({ ...state, personalIdDocUploaded: true });
    props.onDropPersonalIdDoc(event, acceptedFiles);
  };

  const onDropMedicalCertificationDoc = (event, acceptedFiles) => {
    setState({ ...state, medicalCertificationDocUploaded: true });
    props.onDropMedicalCertificationDoc(event, acceptedFiles);
  };

  const submit = (event, errors, values) => {
    setState({ ...state, formConfirmed: true });
    if (!state.personalIdDocUploaded || !state.medicalCertificationDocUploaded) {
      return;
    }

    props.nextStepHandler(event, errors, values);
  };

  return (
    <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
      <AvForm model={props.isNew ? {} : props.entity} onSubmit={submit}>
        <h4 className="sheet-title">Documenti</h4>
        <div style={{ marginBottom: '1rem' }}>
          <Label for="race-personalIdDoc">Documento d'identità</Label>
          <FileUploader
            id="race-presonalIdDoc"
            onDrop={onDropPersonalIdDoc}
            previewUrl={!props.isNew ? props.entity.binaryLogoUrl : null}
          />
        </div>
        <div style={{ marginBottom: '1rem' }}>
          <Label for="race-medicalCertification">Certificato Medico</Label>
          <FileUploader
            id="race-medicalCertification"
            onDrop={onDropMedicalCertificationDoc}
            previewUrl={!props.isNew ? props.entity.binaryLogoUrl : null}
          />
        </div>
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

export default documentsDataSection;
