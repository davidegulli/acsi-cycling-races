import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './file.reducer';
import { IFile } from 'app/shared/model/file.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FileDetail extends React.Component<IFileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            File [<b>{fileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="title">Title</span>
            </dt>
            <dd>{fileEntity.title}</dd>
            <dt>
              <span id="type">Type</span>
            </dt>
            <dd>{fileEntity.type}</dd>
            <dt>
              <span id="mimeType">Mime Type</span>
            </dt>
            <dd>{fileEntity.mimeType}</dd>
            <dt>
              <span id="binary">Binary</span>
            </dt>
            <dd>
              {fileEntity.binary ? (
                <div>
                  <a onClick={openFile(fileEntity.binaryContentType, fileEntity.binary)}>Open&nbsp;</a>
                  <span>
                    {fileEntity.binaryContentType}, {byteSize(fileEntity.binary)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="url">Url</span>
            </dt>
            <dd>{fileEntity.url}</dd>
            <dt>Race</dt>
            <dd>{fileEntity.raceId ? fileEntity.raceId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/file" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/file/${fileEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ file }: IRootState) => ({
  fileEntity: file.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FileDetail);
