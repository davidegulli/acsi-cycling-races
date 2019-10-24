import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import Autosuggest from 'react-autosuggest';

interface ITeamDataSection {
  entity: any;
  isNew: boolean;
  updating: boolean;
  stepIndex: number;
  activeStep: number;
  stepsLength: number;
  nextStepHandler: any;
  prevStepHandler: any;
  cancelUrl: string;
  category: any;
  onTeamSuggestionSearchHandler: any;
  onTeamSuggestionClearRequested: any;
  teamsSuggestions: any;
}

const teamDataSection = (props: ITeamDataSection) => {
  const [value, setValue] = React.useState('');
  const [teamId, setTeamId] = React.useState(props.entity.teamId);
  const [teamCode, setTeamCode] = React.useState(props.entity.teamCode);
  const [teamName, setTeamName] = React.useState(props.entity.teamName);

  const onTeamCodeChange = (event, { newValue }) => {
    console.log(newValue);
    setValue(newValue);
  };

  const getSuggestionValue = suggestion => {
    setTeamId(suggestion.id);
    setTeamCode(suggestion.code);
    setTeamName(suggestion.name);

    return suggestion.code;
  };

  const renderSuggestion = suggestion => (
    <div>
      {suggestion.code} - {suggestion.name}
    </div>
  );

  const inputProps = {
    placeholder: 'Inserisci il codice del tuo team',
    value,
    onChange: onTeamCodeChange
  };

  return (
    <div className={props.activeStep !== props.stepIndex ? 'd-none' : ''}>
      <AvForm model={props.isNew ? {} : props.entity} onSubmit={props.nextStepHandler}>
        <h4 className="sheet-title">Associazione di Appartenza</h4>
        <Row>
          <Col>
            <AvInput name="teamId" type="hidden" value={teamId} />
            <AvInput name="teamCode" type="hidden" value={teamCode} />
            <AvGroup>
              <Label id="teamCodeLabel" for="race-subscription-teamCode">
                Codice Associazione
              </Label>
              <Autosuggest
                suggestions={props.teamsSuggestions}
                onSuggestionsFetchRequested={props.onTeamSuggestionSearchHandler}
                onSuggestionsClearRequested={props.onTeamSuggestionClearRequested}
                getSuggestionValue={getSuggestionValue}
                renderSuggestion={renderSuggestion}
                inputProps={inputProps}
              />
            </AvGroup>
          </Col>
          <Col>
            <AvGroup>
              <Label id="teamNameLabel" for="race-subscription-teamName">
                Nome Associazione
              </Label>
              <AvField
                id="race-subscription-teamName"
                type="string"
                className="form-control"
                name="teamName"
                validate={{
                  required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                }}
                value={teamName}
              />
            </AvGroup>
          </Col>
        </Row>
        <Row>
          <Col>
            <AvGroup>
              <Label id="athleteIdLabel" for="race-subscription-athleteId">
                Numero di Tesseramento
              </Label>
              <AvField
                id="race-subscription-athleteId"
                type="text"
                name="athleteId"
                validate={{
                  required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                }}
              />
            </AvGroup>
          </Col>
          <Col>
            <AvGroup>
              <Label id="categoryLabel" for="race-subscription-category">
                Categoria
              </Label>
              <AvField id="race-subscription-category" type="text" name="category" readOnly value={props.category.name} />
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
};

export default teamDataSection;

/*
            <AvField
              id="race-subscription-teamCode"
              type="string"
              className="form-control"
              name="teamCode"
              validate={{
                required: { value: true, errorMessage: 'Il campo è obbligatorio' }
              }}
            />
*/
