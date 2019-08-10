import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './acsi-team.reducer';
import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAcsiTeamUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAcsiTeamUpdateState {
  isNew: boolean;
}

export class AcsiTeamUpdate extends React.Component<IAcsiTeamUpdateProps, IAcsiTeamUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { acsiTeamEntity } = this.props;
      const entity = {
        ...acsiTeamEntity,
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
    this.props.history.push('/entity/acsi-team');
  };

  render() {
    const { acsiTeamEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.acsiTeam.home.createOrEditLabel" className="sheet-title">
              Associazione ACSI
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : acsiTeamEntity} onSubmit={this.saveEntity}>
                <AvGroup>
                  <Label id="codeLabel" for="acsi-team-code">
                    Codice
                  </Label>
                  <AvField
                    id="acsi-team-code"
                    type="text"
                    name="code"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="acsi-team-name">
                    Nome
                  </Label>
                  <AvField
                    id="acsi-team-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio.' }
                    }}
                  />
                </AvGroup>
                <h4 id="acsiCyclingRacesApp.acsiTeam.home.createOrEditLabel" className="sheet-title">
                  Dati Responsabile
                </h4>
                <AvGroup>
                  <Label id="managerNameIdLabel" for="acsiTeamManagerName">
                    Nome
                  </Label>
                  <AvField
                    id="acsiTeamManagerName"
                    type="text"
                    name="managerName"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio.' }
                    }}
                    readOnly={!isNew}
                    helpMessage="Inserisci il nome del responsabile dell'associzione"
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="managerSurameLabel" for="acsiTeamManagerSurname">
                    Cognome
                  </Label>
                  <AvField
                    id="acsiTeamManagerSurname"
                    type="text"
                    name="managerSurname"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio.' }
                    }}
                    readOnly={!isNew}
                    helpMessage="Inserisci il cognome del responsabile dell'associzione"
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="managerEmailLabel" for="acsiTeamManagerEmail">
                    Email
                  </Label>
                  <AvField
                    id="acsiTeamManagerEmail"
                    type="text"
                    name="managerEmail"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio.' },
                      email: true
                    }}
                    readOnly={!isNew}
                    helpMessage="Inserisci l'indirizzo email del responsabile dell'associzione, 
                                 all'indirizzo indicato verrà mandata una mail per l'accesso al 
                                 portale di gestione delle gare acsi"
                  />
                </AvGroup>
                <div className="form-button-holder">
                  <Button tag={Link} id="cancel-save" to="/entity/acsi-team" replace>
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">Back</span>
                  </Button>
                  &nbsp;
                  <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp; Save
                  </Button>
                </div>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  acsiTeamEntity: storeState.acsiTeam.entity,
  loading: storeState.acsiTeam.loading,
  updating: storeState.acsiTeam.updating,
  updateSuccess: storeState.acsiTeam.updateSuccess
});

const mapDispatchToProps = {
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
)(AcsiTeamUpdate);
