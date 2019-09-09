import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { setFileData } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRaceType } from 'app/shared/model/race-type.model';
import { getEntities as getRaceTypes } from 'app/entities/race-type/race-type.reducer';
import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
import { getEntityByUserLogged as getAcsiTeam } from 'app/entities/acsi-team/acsi-team.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './race.reducer';
import { IRace } from 'app/shared/model/race.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import ImageUploader from '../../shared/component/image-uploader';

export interface IRaceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRaceUpdateState {
  isNew: boolean;
  typeId: string;
  acsiTeamId: string;
}

export class RaceUpdate extends React.Component<IRaceUpdateProps, IRaceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      typeId: '0',
      acsiTeamId: '0',
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

    this.props.getRaceTypes();
    this.props.getAcsiTeam(0);
  }

  saveEntity = (event, errors, values) => {
    values.subscriptionExpirationDate = convertDateTimeToServer(values.subscriptionExpirationDate);

    if (errors.length === 0) {
      const { raceEntity } = this.props;
      const entity = {
        ...raceEntity,
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
    this.props.history.push('/entity/race');
  };

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  onDropLogoImage = (event, acceptedFiles) => {
    setFileData(event, (contentType, data) => this.props.setBlob('binaryLogo', data, contentType), true);
  };

  onDropCoverImage = (event, acceptedFiles) => {
    setFileData(event, (contentType, data) => this.props.setBlob('binaryCover', data, contentType), true);
  };

  onDropPathMapImage = (event, acceptedFiles) => {
    setFileData(event, (contentType, data) => this.props.setBlob('binaryPathMap', data, contentType), true);
  };

  render() {
    const { raceEntity, raceTypes, acsiTeam, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.race.home.createOrEditLabel" className="sheet-title">
              Gara
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : raceEntity} onSubmit={this.saveEntity}>
                <AvGroup>
                  <Label id="nameLabel" for="race-name">
                    Nome
                  </Label>
                  <AvField
                    id="race-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <h4 className="sheet-title">Dati Associazione</h4>
                <AvGroup>
                  <Label for="race-acsiTeamCode">Codice</Label>
                  <AvField id="race-acsiTeamId" type="hidden" name="acsiTeamId" value={acsiTeam.id} />
                  <AvField id="race-acsiTeamCode" type="text" className="form-control" name="acsiTeamCode" readOnly value={acsiTeam.code} />
                </AvGroup>
                <AvGroup>
                  <Label for="race-acsiTeamName">Nome</Label>
                  <AvField id="race-acsiTeamName" type="text" className="form-control" name="acsiTeamName" readOnly value={acsiTeam.name} />
                </AvGroup>
                <h4 className="sheet-title">Contatti Organizzatore</h4>
                <AvGroup>
                  <Label for="race-contactName">Nominativo</Label>
                  <AvField
                    id="race-contactName"
                    type="text"
                    className="form-control"
                    name="contactName"
                    helpMessage="Inserisci il nominativo del contatto responsabile dell'organizzazione della gara"
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="race-contactEmail">E-Mail</Label>
                  <AvField id="race-contactEmail" type="text" className="form-control" name="contactEmail" />
                </AvGroup>
                <AvGroup>
                  <Label for="race-contactPhone">Telefono</Label>
                  <AvField id="race-contactPhone" type="text" className="form-control" name="contactPhone" />
                </AvGroup>
                <h4 className="sheet-title">Informazioni Gara</h4>
                <AvGroup>
                  <Label id="dateLabel" for="race-date">
                    Data
                  </Label>
                  <AvField
                    id="race-date"
                    type="date"
                    className="form-control"
                    name="date"
                    placeholder={'GG-MM-AAAA'}
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="locationLabel" for="race-location">
                    Luogo
                  </Label>
                  <AvField
                    id="race-location"
                    type="text"
                    name="location"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="race-address">
                    Indirizzo
                  </Label>
                  <AvField
                    id="race-address"
                    type="text"
                    name="address"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="race-description">
                    Descrizione
                  </Label>
                  <AvField id="race-description" type="textarea" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="infoLabel" for="race-info">
                    Informazioni Utili
                  </Label>
                  <AvField id="race-info" type="textarea" name="info" />
                </AvGroup>
                <AvGroup>
                  <Label id="rulesLabel" for="race-rules">
                    Regolamento
                  </Label>
                  <AvField id="race-rules" type="textarea" name="rules" />
                </AvGroup>
                <AvGroup>
                  <Label id="subscriptionExpirationDateLabel" for="race-subscriptionExpirationDate">
                    Data Chiusura Iscrizioni
                  </Label>
                  <AvField
                    id="race-subscriptionExpirationDate"
                    type="date"
                    className="form-control"
                    name="subscriptionExpirationDate"
                    placeholder={'GG-MM-AAAA'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.raceEntity.subscriptionExpirationDate)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="race-type">Disciplina</Label>
                  <AvField id="race-type" type="select" className="form-control" name="typeId">
                    <option value="" key="0" />
                    {raceTypes
                      ? raceTypes.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvField>
                </AvGroup>
                <h4 className="sheet-title">Immagini</h4>
                <div style={{ marginBottom: '1rem' }}>
                  <Label for="race-logoImage">Logo</Label>
                  <ImageUploader
                    id="race-logoImage"
                    onDrop={this.onDropLogoImage}
                    previewUrl={!this.state.isNew ? this.props.raceEntity.binaryLogoUrl : null}
                  />
                </div>
                <div style={{ marginBottom: '1rem' }}>
                  <Label for="race-coverImage">Immagine di Copertina</Label>
                  <ImageUploader
                    id="race-coverImage"
                    onDrop={this.onDropCoverImage}
                    previewUrl={!this.state.isNew ? this.props.raceEntity.binaryCoverUrl : null}
                  />
                </div>
                <div style={{ marginBottom: '1rem' }}>
                  <Label for="race-mapPathImage">Mappa del Percorso</Label>
                  <ImageUploader
                    id="race-mapPathImage"
                    onDrop={this.onDropPathMapImage}
                    previewUrl={!this.state.isNew ? this.props.raceEntity.binaryPathMapUrl : null}
                  />
                </div>
                <div className="form-button-holder">
                  <Button tag={Link} id="cancel-save" to="/entity/race" replace>
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">Indietro</span>
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

/*
               <AvGroup>
                  <Label id="addressLabel" for="race-address">
                    Address
                  </Label>
                  <AvField
                    id="race-address"
                    type="text"
                    name="address"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="latitudeLabel" for="race-latitude">
                    Latitude
                  </Label>
                  <AvField id="race-latitude" type="string" className="form-control" name="latitude" />
                </AvGroup>
                <AvGroup>
                  <Label id="longitudeLabel" for="race-longitude">
                    Longitude
                  </Label>
                  <AvField id="race-longitude" type="string" className="form-control" name="longitude" />
                </AvGroup>

                <AvGroup>
                  <Label id="attributesLabel" for="race-attributes">
                    Attributes
                  </Label>
                  <AvField id="race-attributes" type="text" name="attributes" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="race-status">
                    Status
                  </Label>
                  <AvInput
                    id="race-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && raceEntity.status) || 'PUBLISHED'}
                  >
                    <option value="PUBLISHED">PUBLISHED</option>
                    <option value="DRAFT">DRAFT</option>
                    <option value="CANCELED">CANCELED</option>
                    <option value="UNPUBLISHED">UNPUBLISHED</option>
                  </AvInput>
                </AvGroup>

*/

const mapStateToProps = (storeState: IRootState) => ({
  raceTypes: storeState.raceType.entities,
  acsiTeam: storeState.acsiTeam.entity,
  raceEntity: storeState.race.entity,
  loading: storeState.race.loading,
  updating: storeState.race.updating,
  updateSuccess: storeState.race.updateSuccess
});

const mapDispatchToProps = {
  getRaceTypes,
  getAcsiTeam,
  getEntity,
  updateEntity,
  createEntity,
  setBlob,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceUpdate);
