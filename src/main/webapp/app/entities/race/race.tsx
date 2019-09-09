import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import NoElementFound from '../../shared/component/no-element-found';
import Pagination from '../../shared/component/pagination';
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
import { getSearchEntities, getEntities } from './race.reducer';
import { IRace } from 'app/shared/model/race.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IRaceProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IRaceState extends IPaginationBaseState {
  search: string;
}

export class Race extends React.Component<IRaceProps, IRaceState> {
  state: IRaceState = {
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
      this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  render() {
    const { raceList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="race-heading" className="list-title">
          Gare Organizzate
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
                  <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity ml-1" id="jh-create-entity">
                    <FontAwesomeIcon icon="plus" />
                  </Link>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          {raceList && raceList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand">Logo</th>
                  <th className="hand" onClick={this.sort('name')}>
                    Gara <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Disciplina <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('date')}>
                    Data <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('location')}>
                    Luogo <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('subscriptionExpirationDate')}>
                    Scadenza Iscrizioni <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('status')}>
                    Stato <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {raceList.map((race, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <img src={race.binaryLogoUrl} style={{ height: '45px', with: '45px' }} />
                    </td>
                    <td>{race.name}</td>
                    <td>{race.typeName}</td>
                    <td>
                      <TextFormat type="date" value={race.date} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{race.location}</td>
                    <td>
                      <TextFormat type="date" value={race.subscriptionExpirationDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{race.status}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${race.id}`} color="info" size="sm" className="ml-1" title="Anteprima">
                          <FontAwesomeIcon icon="eye" />
                        </Button>
                        <Button tag={Link} to={`${match.url}/${race.id}/edit`} color="primary" size="sm" className="ml-1" title="Modifica">
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button tag={Link} to={`${match.url}/${race.id}/delete`} color="danger" size="sm" className="ml-1" title="Elimina">
                          <FontAwesomeIcon icon="trash" />
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
        <div className={raceList && raceList.length > 0 ? '' : 'd-none'}>
          <Pagination
            activePage={this.state.activePage}
            totalItems={totalItems}
            handlePagination={this.handlePagination}
            itemsPerPage={this.state.itemsPerPage}
          />
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ race }: IRootState) => ({
  raceList: race.entities,
  totalItems: race.totalItems
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
)(Race);
