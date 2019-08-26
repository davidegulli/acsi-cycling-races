import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './non-acsi-team.reducer';
import { INonAcsiTeam } from 'app/shared/model/non-acsi-team.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface INonAcsiTeamUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface INonAcsiTeamUpdateState {
  isNew: boolean;
}

export class NonAcsiTeamUpdate extends React.Component<INonAcsiTeamUpdateProps, INonAcsiTeamUpdateState> {
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
      const { nonAcsiTeamEntity } = this.props;
      const entity = {
        ...nonAcsiTeamEntity,
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
    this.props.history.push('/entity/non-acsi-team');
  };

  render() {
    const { nonAcsiTeamEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.nonAcsiTeam.home.createOrEditLabel" className="sheet-title">
              Associazione Non Affiliata
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : nonAcsiTeamEntity} onSubmit={this.saveEntity}>
                <AvGroup>
                  <Label id="codeLabel" for="non-acsi-team-code">
                    Codice
                  </Label>
                  <AvField
                    id="non-acsi-team-code"
                    type="text"
                    name="code"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="non-acsi-team-name">
                    Nome
                  </Label>
                  <AvField
                    id="non-acsi-team-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <div className="form-button-holder">
                  <Button tag={Link} id="cancel-save" to="/entity/non-acsi-team">
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">Indetro</span>
                  </Button>
                  &nbsp;
                  <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp; Salva
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
  nonAcsiTeamEntity: storeState.nonAcsiTeam.entity,
  loading: storeState.nonAcsiTeam.loading,
  updating: storeState.nonAcsiTeam.updating,
  updateSuccess: storeState.nonAcsiTeam.updateSuccess
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
)(NonAcsiTeamUpdate);
