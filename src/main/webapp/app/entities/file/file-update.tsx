import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRace } from 'app/shared/model/race.model';
import { getEntities as getRaces } from 'app/entities/race/race.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './file.reducer';
import { IFile } from 'app/shared/model/file.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFileUpdateState {
  isNew: boolean;
  raceId: string;
}

export class FileUpdate extends React.Component<IFileUpdateProps, IFileUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      raceId: '0',
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

    this.props.getRaces();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { fileEntity } = this.props;
      const entity = {
        ...fileEntity,
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
    this.props.history.push('/entity/file');
  };

  render() {
    const { fileEntity, races, loading, updating } = this.props;
    const { isNew } = this.state;

    const { binary, binaryContentType } = fileEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.file.home.createOrEditLabel">Create or edit a File</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : fileEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="file-id">ID</Label>
                    <AvInput id="file-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="titleLabel" for="file-title">
                    Title
                  </Label>
                  <AvField id="file-title" type="text" name="title" />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="file-type">
                    Type
                  </Label>
                  <AvInput
                    id="file-type"
                    type="select"
                    className="form-control"
                    name="type"
                    value={(!isNew && fileEntity.type) || 'COVER_IMAGE'}
                  >
                    <option value="COVER_IMAGE">COVER_IMAGE</option>
                    <option value="LOGO_IMAGE">LOGO_IMAGE</option>
                    <option value="PATH_IMAGE">PATH_IMAGE</option>
                    <option value="RANKING_PDF">RANKING_PDF</option>
                    <option value="ATHLETE_ID_DOC">ATHLETE_ID_DOC</option>
                    <option value="MEDICAL_CERTIFICATION">MEDICAL_CERTIFICATION</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="mimeTypeLabel" for="file-mimeType">
                    Mime Type
                  </Label>
                  <AvField
                    id="file-mimeType"
                    type="text"
                    name="mimeType"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="binaryLabel" for="binary">
                      Binary
                    </Label>
                    <br />
                    {binary ? (
                      <div>
                        <a onClick={openFile(binaryContentType, binary)}>Open</a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {binaryContentType}, {byteSize(binary)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('binary')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_binary" type="file" onChange={this.onBlobChange(false, 'binary')} />
                    <AvInput type="hidden" name="binary" value={binary} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="file-url">
                    Url
                  </Label>
                  <AvField id="file-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <Label for="file-race">Race</Label>
                  <AvInput id="file-race" type="select" className="form-control" name="raceId">
                    <option value="" key="0" />
                    {races
                      ? races.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/file" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  races: storeState.race.entities,
  fileEntity: storeState.file.entity,
  loading: storeState.file.loading,
  updating: storeState.file.updating,
  updateSuccess: storeState.file.updateSuccess
});

const mapDispatchToProps = {
  getRaces,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FileUpdate);
