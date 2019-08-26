import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './non-acsi-team.reducer';
import { INonAcsiTeam } from 'app/shared/model/non-acsi-team.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import NoElementFound from 'app/shared/component/no-element-found';
import Pagination from 'app/shared/component/pagination';

export interface INonAcsiTeamProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface INonAcsiTeamState extends IPaginationBaseState {
  search: string;
}

export class NonAcsiTeam extends React.Component<INonAcsiTeamProps, INonAcsiTeamState> {
  state: INonAcsiTeamState = {
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
    const { nonAcsiTeamList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="non-acsi-team-heading" className="list-title">
          Associazioni Non Affiliate
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
          {nonAcsiTeamList && nonAcsiTeamList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('code')}>
                    Codice <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('name')}>
                    Nome <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {nonAcsiTeamList.map((nonAcsiTeam, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{nonAcsiTeam.code}</td>
                    <td>{nonAcsiTeam.name}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`${match.url}/${nonAcsiTeam.id}/edit`}
                          color="primary"
                          size="sm"
                          className="ml-1"
                          title="Modifica"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${nonAcsiTeam.id}/delete`}
                          color="danger"
                          size="sm"
                          className="ml-1"
                          title="Modifica"
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
        <div className={nonAcsiTeamList && nonAcsiTeamList.length > 0 ? '' : 'd-none'}>
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
                        <Button tag={Link} to={`${match.url}/${nonAcsiTeam.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>

*/

const mapStateToProps = ({ nonAcsiTeam }: IRootState) => ({
  nonAcsiTeamList: nonAcsiTeam.entities,
  totalItems: nonAcsiTeam.totalItems
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
)(NonAcsiTeam);
