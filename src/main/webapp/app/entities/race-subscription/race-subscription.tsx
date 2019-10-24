import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  ICrudSearchAction,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  JhiPagination,
  JhiItemCount
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './race-subscription.reducer';
import { IRaceSubscription } from 'app/shared/model/race-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import Pagination from '../../shared/component/pagination';
import NoElementFound from '../../shared/component/no-element-found';

export interface IRaceSubscriptionProps extends StateProps, DispatchProps, RouteComponentProps<{ raceId: string }> {
  context: any;
}

export interface IRaceSubscriptionState extends IPaginationBaseState {
  search: string;
}

export class RaceSubscription extends React.Component<IRaceSubscriptionProps, IRaceSubscriptionState> {
  state: IRaceSubscriptionState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.setState({ activePage: 1 }, () => {
        const { activePage, itemsPerPage, sort, order, search } = this.state;
        this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
      });
    }
  };

  clear = () => {
    this.setState({ search: '', activePage: 1 }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order, search } = this.state;
    if (search) {
      this.props.getSearchEntities(search, activePage - 1, itemsPerPage, `${sort},${order}`);
    } else {
      this.props.getEntities(this.props.match.params.raceId, activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  render() {
    const { raceSubscriptionList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="race-subscription-heading" className="list-title">
          Iscrizioni
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon ml-1">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon ml-1" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                  <a
                    href={`/api/race-subscriptions/race/${this.props.match.params.raceId}/list/excel/download`}
                    className="btn btn-primary  jh-create-entity ml-3"
                    title="Esporta Excel"
                  >
                    <FontAwesomeIcon icon="file-excel" />
                  </a>
                  <a
                    href={`/api/race-subscriptions/race/${this.props.match.params.raceId}/list/pdf/download`}
                    className="btn btn-primary  jh-create-entity ml-1"
                    title="Esporta Pdf"
                  >
                    <FontAwesomeIcon icon="file-pdf" />
                  </a>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          {raceSubscriptionList && raceSubscriptionList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('date')}>
                    Data Iscrizione <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('name')}>
                    Atleta <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand">Contatti</th>
                  <th className="hand" onClick={this.sort('category')}>
                    Categoria <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('teamId')}>
                    Società <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {raceSubscriptionList.map((raceSubscription, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <TextFormat type="date" value={raceSubscription.date} format={APP_DATE_FORMAT} />
                    </td>
                    <td>
                      <div>
                        <span className="pr-1">{raceSubscription.name}</span>
                        <span>{raceSubscription.surname}</span>
                      </div>
                      <div>{raceSubscription.birthDate}</div>
                    </td>
                    <td>
                      <div>{raceSubscription.email}</div>
                      <div>{raceSubscription.phone}</div>
                    </td>
                    <td>{raceSubscription.category}</td>
                    <td>{raceSubscription.teamName}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`/entity/race-subscription/${raceSubscription.id}`}
                          color="info"
                          size="sm"
                          className="ml-1"
                          title="Dettaglio"
                        >
                          <FontAwesomeIcon icon="eye" />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <NoElementFound />
          )}
        </div>
        <div className={raceSubscriptionList && raceSubscriptionList.length > 0 ? '' : 'd-none'}>
          <Pagination
            activePage={this.state.activePage}
            handlePagination={this.handlePagination}
            itemsPerPage={this.state.itemsPerPage}
            totalItems={this.props.totalItems}
          />
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ raceSubscription }: IRootState) => ({
  raceSubscriptionList: raceSubscription.entities,
  totalItems: raceSubscription.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceSubscription);

/*
                  <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity ml-1" id="jh-create-entity">
                    <FontAwesomeIcon icon="plus" />
                  </Link>
                        <Button
                          tag={Link}
                          to={`${match.url}/${raceSubscription.id}/edit`}
                          color="primary"
                          size="sm"
                          className="ml-1"
                          title="Modifica"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${raceSubscription.id}/delete`}
                          color="danger"
                          size="sm"
                          className="ml-1"
                          title="Elimina"
                        >
                          <FontAwesomeIcon icon="trash" />
                        </Button>

*/
