import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './acsi-team.reducer';
import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import NoElementFound from '../../shared/component/no-element-found';

export interface IAcsiTeamProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IAcsiTeamState {
  search: string;
}

export class AcsiTeam extends React.Component<IAcsiTeamProps, IAcsiTeamState> {
  state: IAcsiTeamState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.setState({ search: '' }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { acsiTeamList, match } = this.props;
    return (
      <div>
        <h2 id="acsi-team-heading" className="list-title">
          Associazioni Acsi
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Nuova
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput
                    type="text"
                    name="search"
                    value={this.state.search}
                    onChange={this.handleSearch}
                    placeholder="Ricerca"
                    className="button-rigth-margin"
                  />
                  <Button className="input-group-addon button-rigth-margin">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
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
                  <th>Code</th>
                  <th>Name</th>
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
                          to={`${match.url}/${acsiTeam.id}`}
                          color="info"
                          size="sm"
                          title="Dettaglio"
                          className="button-rigth-margin"
                        >
                          <FontAwesomeIcon icon="eye" />
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${acsiTeam.id}/edit`}
                          color="primary"
                          size="sm"
                          title="Modifica"
                          className="button-rigth-margin"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button tag={Link} to={`${match.url}/${acsiTeam.id}/delete`} color="danger" size="sm" title="Elimina">
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
      </div>
    );
  }
}

const mapStateToProps = ({ acsiTeam }: IRootState) => ({
  acsiTeamList: acsiTeam.entities
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
<Button tag={Link} to={`${match.url}/${acsiTeam.id}`} color="link" size="sm"></Button>
*/
