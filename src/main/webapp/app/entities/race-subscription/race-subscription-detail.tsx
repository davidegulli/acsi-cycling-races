import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './race-subscription.reducer';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRaceSubscriptionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RaceSubscriptionDetail extends React.Component<IRaceSubscriptionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { raceSubscriptionEntity } = this.props;
    return (
      <div className="container">
        <Row>
          <Col md="12">
            <h2 className="sheet-title">Dettaglio Iscrizione</h2>
            <dl>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="date">Data e Ora Iscrizione</span>
                  </dt>
                  <dd>
                    <TextFormat value={raceSubscriptionEntity.date} type="date" format={APP_DATE_FORMAT} />
                  </dd>
                </Col>
                <Col md="2">
                  <dt>Gara</dt>
                  <dd>{raceSubscriptionEntity.raceId ? raceSubscriptionEntity.raceId : ''}</dd>
                </Col>
              </Row>
              <h4 className="sheet-title">Dati Atleta</h4>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="name">Nome</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.name}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="surname">Cognome</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.surname}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="birthDate">Data di Nascita</span>
                  </dt>
                  <dd>
                    <TextFormat value={raceSubscriptionEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
                  </dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="birthPlace">Luogo di Nascita</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.birthPlace}</dd>
                </Col>
              </Row>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="taxCode">Codice Fiscale</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.taxCode}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="gender">Sesso</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.gender}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="gender">Categoria</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.category}</dd>
                </Col>
              </Row>
              <h4 className="sheet-title">Contatti</h4>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="email">Email</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.email}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="phone">Numero di Telefono</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.phone}</dd>
                </Col>
              </Row>
              <h4 className="sheet-title">Gara</h4>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="subcriptionTypeId">Subcription Type Id</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.subcriptionTypeId}</dd>
                </Col>
                <Col>
                  <dt>
                    <span id="pathType">Path Type</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.pathType}</dd>
                </Col>
              </Row>
              <h4 className="sheet-title">Società</h4>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="teamId">Codice Società</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.teamId}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="teamId">Nome Società</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.teamId}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="athleteId">Athlete Id</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.athleteId}</dd>
                </Col>
              </Row>
              <h4 className="sheet-title">Dati del Pagamento</h4>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="paymentType">Metodo</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.paymentType}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="payedPrice">Prezzo Iscrizione</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.payedPrice}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="payed">Pagato</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.payed ? 'SI' : 'NO'}</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="paymentReceivedCode">Codice Conferma</span>
                  </dt>
                  <dd>{raceSubscriptionEntity.paymentReceivedCode}</dd>
                </Col>
              </Row>
              <h4 className="sheet-title">Documenti</h4>
              <Row>
                <Col md="2">
                  <dt>
                    <span id="paymentType">Documento d'identità</span>
                  </dt>
                  <dd>anteprima doc</dd>
                </Col>
                <Col md="2">
                  <dt>
                    <span id="payedPrice">Certificato Medico</span>
                  </dt>
                  <dd>anteprima doc</dd>
                </Col>
              </Row>
            </dl>
            <div className="form-button-holder">
              <Button tag={Link} to={`/entity/race-subscription/list/${raceSubscriptionEntity.raceId}`} replace color="info">
                <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
              </Button>
            </div>
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ raceSubscription }: IRootState) => ({
  raceSubscriptionEntity: raceSubscription.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceSubscriptionDetail);
