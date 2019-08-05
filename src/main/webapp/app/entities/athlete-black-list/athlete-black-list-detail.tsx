import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './athlete-black-list.reducer';
import { IAthleteBlackList } from 'app/shared/model/athlete-black-list.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAthleteBlackListDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AthleteBlackListDetail extends React.Component<IAthleteBlackListDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { athleteBlackListEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            AthleteBlackList [<b>{athleteBlackListEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{athleteBlackListEntity.name}</dd>
            <dt>
              <span id="surname">Surname</span>
            </dt>
            <dd>{athleteBlackListEntity.surname}</dd>
            <dt>
              <span id="birthDate">Birth Date</span>
            </dt>
            <dd>
              <TextFormat value={athleteBlackListEntity.birthDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="taxCode">Tax Code</span>
            </dt>
            <dd>{athleteBlackListEntity.taxCode}</dd>
          </dl>
          <Button tag={Link} to="/entity/athlete-black-list" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/athlete-black-list/${athleteBlackListEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ athleteBlackList }: IRootState) => ({
  athleteBlackListEntity: athleteBlackList.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AthleteBlackListDetail);
