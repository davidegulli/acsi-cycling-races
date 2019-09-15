import './race-subscription.scss';

import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField, AvRadioGroup, AvRadio } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRace } from 'app/shared/model/race.model';
import { getEntity as getRace } from 'app/entities/race/race.reducer';
import { getEntity, updateEntity, createEntity, reset } from './race-subscription.reducer';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import RaceHeader from '../race/race-header';

export interface IRaceSubscriptionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string; raceId: string }> {}

export interface IRaceSubscriptionUpdateState {
  isNew: boolean;
  raceId: string;
  step: number;
  values: any;
  errors: any;
}

export class RaceSubscriptionUpdate extends React.Component<IRaceSubscriptionUpdateProps, IRaceSubscriptionUpdateState> {
  form = {};

  constructor(props) {
    super(props);
    this.state = {
      raceId: this.props.match.params.raceId,
      isNew: !this.props.match.params || !this.props.match.params.id,
      step: 0,
      values: {},
      errors: []
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getRace(this.props.match.params.raceId);
  }

  saveEntity = (errors, values) => {
    values.date = convertDateTimeToServer(values.date);

    console.log(values);

    if (errors.length === 0) {
      const { raceSubscriptionEntity } = this.props;
      const entity = {
        ...raceSubscriptionEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/race-subscription');
  };

  nextStepHandler = (event, errors, values) => {
    if (errors.length === 0) {
      if (this.state.step < 4) {
        const currentState = this.state.step;
        this.setState({ step: currentState + 1 });
      }

      const currentErrors = this.state.errors;
      const updateErrors = [...currentErrors, ...errors];

      const currentValues = this.state.values;
      const updatedValues = { ...currentValues, ...values };
      this.setState({ values: updatedValues, errors: updateErrors });

      if (this.state.step === 4) {
        this.saveEntity(updateErrors, updatedValues);
      }
    }
  };

  prevStepHandler = () => {
    const currentState = this.state.step;
    this.setState({ step: currentState - 1 });
  };

  render() {
    const { raceSubscriptionEntity, race, loading, updating } = this.props;
    const { isNew } = this.state;

    const steps = [];

    steps.push(
      <div className={this.state.step !== 0 ? 'd-none' : ''}>
        <AvForm model={isNew ? {} : raceSubscriptionEntity} onSubmit={this.nextStepHandler}>
          <AvInput type="hidden" name="raceId" value={race.id} />
          <h4 className="sheet-title">Informazioni Personali</h4>
          <Row>
            <Col>
              <AvGroup>
                <Label id="nameLabel" for="race-subscription-name">
                  Nome
                </Label>
                <AvField
                  id="race-subscription-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                  }}
                />
              </AvGroup>
            </Col>
            <Col>
              <AvGroup>
                <Label id="surnameLabel" for="race-subscription-surname">
                  Cognome
                </Label>
                <AvField
                  id="race-subscription-surname"
                  type="text"
                  name="surname"
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                  }}
                />
              </AvGroup>
            </Col>
          </Row>
          <Row>
            <Col>
              <AvGroup>
                <Label id="birthDateLabel" for="race-subscription-birthDate">
                  Data di Nascita
                </Label>
                <AvField
                  id="race-subscription-birthDate"
                  type="date"
                  name="birthDate"
                  placeholder={'GG-MM-AAAA'}
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                  }}
                />
              </AvGroup>
            </Col>
            <Col>
              <AvGroup>
                <Label id="birthPlaceLabel" for="race-subscription-birthPlace">
                  Luogo di Nascita
                </Label>
                <AvField
                  id="race-subscription-birthPlace"
                  type="text"
                  name="birthPlace"
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                  }}
                />
              </AvGroup>
            </Col>
          </Row>
          <Row>
            <Col md="3">
              <AvGroup>
                <Label id="genderLabel" for="race-subscription-gender">
                  Sesso
                </Label>
                <AvInput
                  id="race-subscription-gender"
                  type="select"
                  className="form-control"
                  name="gender"
                  value={(!isNew && raceSubscriptionEntity.gender) || 'MALE'}
                >
                  <option value="MALE">MALE</option>
                  <option value="FEMALE">FEMALE</option>
                </AvInput>
              </AvGroup>
            </Col>
            <Col md="9">
              <AvGroup>
                <Label id="taxCodeLabel" for="race-subscription-taxCode">
                  Codice Fiscale
                </Label>
                <AvField
                  id="race-subscription-taxCode"
                  type="text"
                  name="taxCode"
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                  }}
                />
              </AvGroup>
            </Col>
          </Row>
          <div className="form-button-holder">
            <Button tag={Link} id="cancel-save" to="/entity/race-subscription" replace>
              <span className="d-none d-md-inline">Annulla</span>
            </Button>
            <Button color="primary" id="save-entity" disabled={updating} onClick={this.prevStepHandler} className="ml-2">
              Indierto
            </Button>
            <Button color="primary" id="save-entity" type="submit" disabled={updating} className="ml-2">
              Procedi
            </Button>
          </div>
        </AvForm>
      </div>
    );

    steps.push(
      <div className={this.state.step !== 1 ? 'd-none' : ''}>
        <AvForm model={isNew ? {} : raceSubscriptionEntity} onSubmit={this.nextStepHandler}>
          <h4 className="sheet-title">Contatti</h4>
          <Row>
            <Col>
              <AvGroup>
                <Label id="emailLabel" for="race-subscription-email">
                  Email
                </Label>
                <AvField
                  id="race-subscription-email"
                  type="email"
                  name="email"
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                    email: { value: true, errorMessage: 'La mail immessa non è corretta' }
                  }}
                />
              </AvGroup>
            </Col>
            <Col>
              <AvGroup>
                <Label id="phoneLabel" for="race-subscription-phone">
                  Telefono
                </Label>
                <AvField
                  id="race-subscription-phone"
                  type="text"
                  name="phone"
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                  }}
                />
              </AvGroup>
            </Col>
          </Row>
          <div className="form-button-holder">
            <Button tag={Link} id="cancel-save" to="/entity/race-subscription" replace>
              <span className="d-none d-md-inline">Annulla</span>
            </Button>
            <Button color="primary" id="save-entity" disabled={updating} onClick={this.prevStepHandler} className="ml-2">
              Indierto
            </Button>
            <Button color="primary" id="save-entity" type="submit" disabled={updating} className="ml-2">
              Procedi
            </Button>
          </div>
        </AvForm>
      </div>
    );

    steps.push(
      <div className={this.state.step !== 2 ? 'd-none' : ''}>
        <AvForm model={isNew ? {} : raceSubscriptionEntity} onSubmit={this.nextStepHandler}>
          <h4 className="sheet-title">Associazione di Appartenza</h4>
          <Row>
            <Col>
              <AvInput name="teamId" type="hidden" />
              <AvGroup>
                <Label id="teamCodeLabel" for="race-subscription-teamCode">
                  Codice Associazione
                </Label>
                <AvField
                  id="race-subscription-teamCode"
                  type="string"
                  className="form-control"
                  name="teamCode"
                  validate={{
                    required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                  }}
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
                <AvField id="race-subscription-category" type="text" name="category" />
              </AvGroup>
            </Col>
          </Row>
          <div className="form-button-holder">
            <Button tag={Link} id="cancel-save" to="/entity/race-subscription" replace>
              <span className="d-none d-md-inline">Annulla</span>
            </Button>
            <Button color="primary" id="save-entity" disabled={updating} onClick={this.prevStepHandler} className="ml-2">
              Indierto
            </Button>
            <Button color="primary" id="save-entity" type="submit" disabled={updating} className="ml-2">
              Procedi
            </Button>
          </div>
        </AvForm>
      </div>
    );

    steps.push(
      <div className={this.state.step !== 3 ? 'd-none' : ''}>
        <AvForm model={isNew ? {} : raceSubscriptionEntity} onSubmit={this.nextStepHandler}>
          <h4 className="sheet-title">Gara</h4>
          <Row>
            <Col>
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
            </Col>
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
          </Row>
          <div className="form-button-holder">
            <Button tag={Link} id="cancel-save" to="/entity/race-subscription" replace>
              <span className="d-none d-md-inline">Annulla</span>
            </Button>
            <Button color="primary" id="save-entity" disabled={updating} onClick={this.prevStepHandler} className="ml-2">
              Indierto
            </Button>
            <Button color="primary" id="save-entity" type="submit" disabled={updating} className="ml-2">
              Procedi
            </Button>
          </div>
        </AvForm>
      </div>
    );

    steps.push(
      <div className={this.state.step !== 4 ? 'd-none' : ''}>
        <AvForm model={isNew ? {} : raceSubscriptionEntity} onSubmit={this.nextStepHandler}>
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
                            <AvRadio label="Paypal" value="paypal" />
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
                            <AvRadio label="Bonifico Bancario" value="bonifico" />
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
          <div className="form-button-holder">
            <Button tag={Link} id="cancel-save" to="/entity/race-subscription" replace>
              <span className="d-none d-md-inline">Annulla</span>
            </Button>
            <Button color="primary" id="save-entity" disabled={updating} onClick={this.prevStepHandler} className="ml-2">
              Indierto
            </Button>
            <Button color="primary" id="save-entity" type="submit" disabled={updating} className="ml-2">
              Procedi
            </Button>
          </div>
        </AvForm>
      </div>
    );

    return (
      <div>
        <RaceHeader entity={race} showButton={false} />
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <Fragment>
                {steps[0]}
                {steps[1]}
                {steps[2]}
                {steps[3]}
                {steps[4]}
              </Fragment>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

/*
                  <AvInput
                    id="race-subscription-paymentType"
                    type="select"
                    className="form-control"
                    name="paymentType"
                    value={(!isNew && raceSubscriptionEntity.paymentType) || 'PAYPAL'}
                  >
                    <option value="PAYPAL">PAYPAL</option>
                    <option value="CREDIT_TRANSFER">CREDIT_TRANSFER</option>
                  </AvInput>
                  */

const mapStateToProps = (storeState: IRootState) => ({
  race: storeState.race.entity,
  raceSubscriptionEntity: storeState.raceSubscription.entity,
  loading: storeState.raceSubscription.loading,
  updating: storeState.raceSubscription.updating,
  updateSuccess: storeState.raceSubscription.updateSuccess
});

const mapDispatchToProps = {
  getRace,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceSubscriptionUpdate);
