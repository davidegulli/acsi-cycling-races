import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import DocumentUploader from '../../../shared/component/document-uploader';

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

const documentsDataSection = (props: IDocumentsDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Documenti</h4>
      <div style={{ marginBottom: '1rem' }}>
        <Label for="race-personalIdDoc">Documento d'identità</Label>
        <DocumentUploader
          id="race-presonalIdDoc"
          onDrop={props.onDropPersonalIdDoc}
          previewUrl={!props.isNew ? props.entity.binaryLogoUrl : null}
        />
      </div>
      <div style={{ marginBottom: '1rem' }}>
        <Label for="race-medicalCertification">Certificato Medico</Label>
        <DocumentUploader
          id="race-medicalCertification"
          onDrop={props.onDropMedicalCertificationDoc}
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

export default documentsDataSection;
