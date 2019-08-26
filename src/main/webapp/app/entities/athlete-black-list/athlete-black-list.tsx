import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import Pagination from '../../shared/component/pagination';
import NoElementFound from '../../shared/component/no-element-found';
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
import { getSearchEntities, getEntities } from './athlete-black-list.reducer';
import { IAthleteBlackList } from 'app/shared/model/athlete-black-list.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IAthleteBlackListProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IAthleteBlackListState extends IPaginationBaseState {
  search: string;
}

export class AthleteBlackList extends React.Component<IAthleteBlackListProps, IAthleteBlackListState> {
  state: IAthleteBlackListState = {
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
    const { athleteBlackListList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="athlete-black-list-heading" className="list-title">
          Blacklist
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Ricerca" />
                  <Button className="input-group-addon ml-1">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon ml-1" onClick={this.clear}>
                    <FontAwesomeIcon icon="times" />
                  </Button>
                  <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity ml-1" id="jh-create-entity">
                    <FontAwesomeIcon icon="plus" />
                  </Link>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          {athleteBlackListList && athleteBlackListList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('name')}>
                    Nome <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('surname')}>
                    Cognome <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand text-center" onClick={this.sort('birthDate')}>
                    Data di Nascita <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('taxCode')}>
                    Codice Fiscale <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {athleteBlackListList.map((athleteBlackList, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{athleteBlackList.name}</td>
                    <td>{athleteBlackList.surname}</td>
                    <td className="text-center">
                      <TextFormat type="date" value={athleteBlackList.birthDate} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td className="text-uppercase">{athleteBlackList.taxCode}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`${match.url}/${athleteBlackList.id}/edit`}
                          color="primary"
                          size="sm"
                          className="ml-1"
                          title="Modifica"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${athleteBlackList.id}/delete`}
                          color="danger"
                          size="sm"
                          className="ml-1"
                          title="Elimina"
                        >
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
        <div className={athleteBlackListList && athleteBlackListList.length > 0 ? '' : 'd-none'}>
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

/*
                        <Button tag={Link} to={`${match.url}/${athleteBlackList.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>

*/

const mapStateToProps = ({ athleteBlackList }: IRootState) => ({
  athleteBlackListList: athleteBlackList.entities,
  totalItems: athleteBlackList.totalItems
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
)(AthleteBlackList);
