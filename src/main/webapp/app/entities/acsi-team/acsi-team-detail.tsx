import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './acsi-team.reducer';
import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAcsiTeamDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AcsiTeamDetail extends React.Component<IAcsiTeamDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { acsiTeamEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            AcsiTeam [<b>{acsiTeamEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="code">Code</span>
            </dt>
            <dd>{acsiTeamEntity.code}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{acsiTeamEntity.name}</dd>
            <dt>
              <span id="userId">User Id</span>
            </dt>
            <dd>{acsiTeamEntity.userId}</dd>
          </dl>
          <Button tag={Link} to="/entity/acsi-team" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/acsi-team/${acsiTeamEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ acsiTeam }: IRootState) => ({
  acsiTeamEntity: acsiTeam.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AcsiTeamDetail);
