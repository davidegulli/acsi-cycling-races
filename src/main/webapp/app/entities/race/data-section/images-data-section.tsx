import React, { useState } from 'react';
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

const imagesDataSection = (props: IImagesDataSection) => {
  const [formConfirmed, setFormConfirmed] = useState(false);
  const [logoUploaded, setLogoUploaded] = useState(false);
  const [coverUploaded, setCoverUploaded] = useState(false);

  const onDropLogo = (event, acceptedFiles) => {
    setLogoUploaded(true);
    props.onDropLogoImage(event, acceptedFiles);
  };

  const onDropCover = (event, acceptedFiles) => {
    setCoverUploaded(true);
    props.onDropCoverImage(event, acceptedFiles);
  };

  const submit = (event, errors, values) => {
    setFormConfirmed(true);

    if (!logoUploaded || !coverUploaded) {
      return;
    }

    props.nextStepHandler(event, errors, values);
  };

  return (
    <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
      <AvForm model={props.isNew ? {} : props.entity} onSubmit={submit}>
        <h4 className="sheet-title">Immagini</h4>
        <div style={{ marginBottom: '1rem' }}>
          <Label for="race-logoImage">Logo</Label>
          <ImageUploader
            id="race-logoImage"
            onDrop={onDropLogo}
            previewUrl={!props.isNew ? props.entity.binaryLogoUrl : null}
            text="Qui puoi selezionare l'immagine da visualizzare come logo della gara, ti consigliamo di scegliere
                  un'immagine che abbia delle dimensioni approssimativamente di 300x400 pixel, così da garantirne
                  una corretta visualizzaione nella pagina di presentazione della gara"
          />
          {formConfirmed && !logoUploaded ? <div className="invalid-feedback">L'immagine è obbligatoria, selezionane una!</div> : null}
        </div>
        <div style={{ marginBottom: '1rem' }}>
          <Label for="race-coverImage">Immagine di Copertina</Label>
          <ImageUploader
            id="race-coverImage"
            onDrop={onDropCover}
            previewUrl={!props.isNew ? props.entity.binaryCoverUrl : null}
            text="Qui puoi selezionare l'immagine da visualizzare come copertina della gara, ti consigliamo di scegliere
                  un'immagine che abbia delle dimensioni approssimativamente di 300x1024 pixel, così da garantirne
                  una corretta visualizzaione nella pagina di presentazione della gara"
          />
          {formConfirmed && !coverUploaded ? <div className="invalid-feedback">L'immagine è obbligatoria, selezionane una!</div> : null}
        </div>
        <div style={{ marginBottom: '1rem' }}>
          <Label for="race-mapPathImage">Mappa del Percorso</Label>
          <ImageUploader
            id="race-mapPathImage"
            onDrop={props.onDropPathMapImage}
            previewUrl={!props.isNew ? props.entity.binaryPathMapUrl : null}
            text="Qui puoi selezionare l'immagine della mappa del percorso che seguirà la gara, ti consigliamo di scegliere
                  un'immagine che abbia delle dimensioni approssimativamente di 400x400 pixel, così da garantirne
                  una corretta visualizzaione nella pagina di presentazione della gara"
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

export default imagesDataSection;
