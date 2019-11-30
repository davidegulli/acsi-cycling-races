import React from 'react';
import { Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
import StepperButtons from '../../../shared/component/stepper-buttons';
import Autosuggest from 'react-autosuggest';
import { stat } from 'fs';

interface ITeamDataSectionProps {
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

interface ITeamDataSectionState {
  value: string;
  teamId: string;
  teamCode: string;
  teamName: string;
}

class TeamDataSection extends React.Component<ITeamDataSectionProps, ITeamDataSectionState> {
  constructor(props) {
    super(props);
    this.state = {
      value: '',
      teamId: !props.isNew ? props.entity.teamId : '',
      teamCode: !props.isNew ? props.entity.teamCode : '',
      teamName: !props.isNew ? props.entity.teamName : ''
    };
  }

  onTeamCodeChange = (event, { newValue }) => {
    this.setState({
      value: newValue,
      teamCode: newValue
    });
  };

  getSuggestionValue = suggestion => {
    this.setState({
      teamId: suggestion.id,
      teamCode: suggestion.code,
      teamName: suggestion.name
    });

    return suggestion.code;
  };

  renderSuggestion = suggestion => (
    <div id={suggestion.name}>
      {suggestion.code} - {suggestion.name}
    </div>
  );

  render() {
    const inputProps = {
      placeholder: 'Inserisci il codice del tuo team',
      value: this.state.value,
      onChange: this.onTeamCodeChange
    };

    return (
      <div className={this.props.activeStep !== this.props.stepIndex ? 'd-none' : ''}>
        <AvForm model={this.props.isNew ? {} : this.props.entity} onSubmit={this.props.nextStepHandler}>
          <h4 className="sheet-title">Associazione di Appartenenza</h4>
          <Row>
            <Col>
              <AvInput name="teamId" type="hidden" value={this.state.teamId} />
              <AvInput name="teamCode" type="hidden" value={this.state.teamCode} />
              <AvGroup>
                <Label id="teamCodeLabel" for="race-subscription-teamCode">
                  Codice Associazione
                </Label>
                <Autosuggest
                  suggestions={this.props.teamsSuggestions}
                  onSuggestionsFetchRequested={this.props.onTeamSuggestionSearchHandler}
                  onSuggestionsClearRequested={this.props.onTeamSuggestionClearRequested}
                  getSuggestionValue={this.getSuggestionValue}
                  renderSuggestion={this.renderSuggestion}
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
                  value={this.state.teamName}
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
                <AvField id="race-subscription-category" type="text" name="category" readOnly value={this.props.category.name} />
              </AvGroup>
            </Col>
          </Row>
          <StepperButtons
            activeStep={this.props.activeStep}
            stepsLength={this.props.stepsLength}
            updating={this.props.updating}
            prevStepHandler={this.props.prevStepHandler}
            cancelUrl={this.props.cancelUrl}
          />
        </AvForm>
      </div>
    );
  }
}

export default TeamDataSection;

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
