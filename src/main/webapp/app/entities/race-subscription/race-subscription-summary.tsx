import './race-subscription.scss';

import React, { Fragment } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { setFileData, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import { IRace } from 'app/shared/model/race.model';
import { getEntity as getRace } from 'app/entities/race/race.reducer';
import { getEntity } from './race-subscription.reducer';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import RaceHeader from '../race/race-header';
import { Box } from '@material-ui/core';

export interface IRaceSubscriptionSummaryProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string; raceId: string }> {}

export interface IRaceSubscriptionSummaryState {}

export class RaceSubscriptionSummary extends React.Component<IRaceSubscriptionSummaryProps, IRaceSubscriptionSummaryState> {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
    this.props.getRace(this.props.match.params.raceId);
  }

  render() {
    const { loading, race, subscription } = this.props;

    return (
      <div>
        <RaceHeader entity={race} showButton={false} />
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <Box className="mt-5">
                <h3>Complimenti {subscription.name} sei iscritto alla gara!</h3>
                <h6>A breve riceverai una mail con tutti i dettagli necessari per completare l'iscrizione procedendo al pagamento</h6>
                <h6>Se hai bisogno di qualsiasi informazione non esitare a contattarci alla seguente mail: info@acsi.com</h6>
              </Box>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  race: storeState.race.entity,
  subscription: storeState.raceSubscription.entity,
  loading: storeState.raceSubscription.loading
});

const mapDispatchToProps = {
  getRace,
  getEntity
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceSubscriptionSummary);
