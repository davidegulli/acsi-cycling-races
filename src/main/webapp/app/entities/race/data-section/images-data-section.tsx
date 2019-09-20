import React from 'react';
import { Label } from 'reactstrap';
import { AvForm } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import ImageUploader from '../../../shared/component/image-uploader';

interface IImagesDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  onDropLogoImage: any;
  onDropCoverImage: any;
  onDropPathMapImage: any;
  cancelUrl: string;
}

const imagesDataSection = (props: IImagesDataSection) => (
  <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
    <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
      <h4 className="sheet-title">Immagini</h4>
      <div style={{ marginBottom: '1rem' }}>
        <Label for="race-logoImage">Logo</Label>
        <ImageUploader id="race-logoImage" onDrop={props.onDropLogoImage} previewUrl={!props.isNew ? props.entity.binaryLogoUrl : null} />
      </div>
      <div style={{ marginBottom: '1rem' }}>
        <Label for="race-coverImage">Immagine di Copertina</Label>
        <ImageUploader
          id="race-coverImage"
          onDrop={props.onDropCoverImage}
          previewUrl={!props.isNew ? props.entity.binaryCoverUrl : null}
        />
      </div>
      <div style={{ marginBottom: '1rem' }}>
        <Label for="race-mapPathImage">Mappa del Percorso</Label>
        <ImageUploader
          id="race-mapPathImage"
          onDrop={props.onDropPathMapImage}
          previewUrl={!props.isNew ? props.entity.binaryPathMapUrl : null}
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

export default imagesDataSection;
