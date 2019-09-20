import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table, Badge } from 'reactstrap';
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
import { getSearchEntities, getEntities } from '../../entities/race/race.reducer';
import { IRace } from 'app/shared/model/race.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { Chip } from '@material-ui/core';
import moment from 'moment';
import StyledChip from 'app/modules/events/subscription-status-chip';
import SubscriptionStatusChip from './subscription-status-chip';

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
          Eventi in Programma
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
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          {raceList && raceList.length > 0 ? (
            <Table responsive striped>
              <thead>
                <tr>
                  <th className="hand">Logo</th>
                  <th className="hand" onClick={this.sort('name')}>
                    Gara <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('date')}>
                    Data <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('location')}>
                    Località <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" />
                </tr>
              </thead>
              <tbody>
                {raceList.map((race, i) => (
                  <tr key={`entity-${i}`} className="align-middle">
                    <td>
                      <img src={race.binaryLogoUrl} style={{ height: '45px', width: '45px' }} />
                    </td>
                    <td>
                      <Button tag={Link} to={`/event/${race.id}`} color="none" size="sm">
                        {race.name}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={race.date} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{race.location}</td>
                    <td>
                      <div className="btn-group flex-btn-group-container">
                        <SubscriptionStatusChip expirationDate={race.subscriptionExpirationDate} />
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
