import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './acsi-team.reducer';
import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import NoElementFound from 'app/shared/component/no-element-found';
import Pagination from '../../shared/component/pagination';

export interface IAcsiTeamProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IAcsiTeamState extends IPaginationBaseState {
  search: string;
}

export class AcsiTeam extends React.Component<IAcsiTeamProps, IAcsiTeamState> {
  state: IAcsiTeamState = {
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
    const { acsiTeamList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="acsi-team-heading" className="list-title">
          Associazioni Affiliate
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
          {acsiTeamList && acsiTeamList.length > 0 ? (
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
                {acsiTeamList.map((acsiTeam, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{acsiTeam.code}</td>
                    <td>{acsiTeam.name}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`${match.url}/${acsiTeam.id}/edit`}
                          color="primary"
                          size="sm"
                          className="ml-1"
                          title="Modifica"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${acsiTeam.id}/delete`}
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
        <div className={acsiTeamList && acsiTeamList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ acsiTeam }: IRootState) => ({
  acsiTeamList: acsiTeam.entities,
  totalItems: acsiTeam.totalItems
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
)(AcsiTeam);

/*
                        <Button tag={Link} to={`${match.url}/${acsiTeam.id}`} color="info" size="sm" 
                                className="ml-1">
                          <FontAwesomeIcon icon="eye" />
                        </Button>

*/
