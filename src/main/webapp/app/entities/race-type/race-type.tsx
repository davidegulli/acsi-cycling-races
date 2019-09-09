import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './race-type.reducer';
import { IRaceType } from 'app/shared/model/race-type.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import NoElementFound from 'app/shared/component/no-element-found';

export interface IRaceTypeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IRaceTypeState {
  search: string;
}

export class RaceType extends React.Component<IRaceTypeProps, IRaceTypeState> {
  state: IRaceTypeState = {
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
    const { raceTypeList, match } = this.props;
    return (
      <div>
        <h2 id="race-type-heading" className="list-title">
          Discipline
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
          {raceTypeList && raceTypeList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Description</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {raceTypeList.map((raceType, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{raceType.name}</td>
                    <td>{raceType.description}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`${match.url}/${raceType.id}/edit`}
                          color="primary"
                          size="sm"
                          className="ml-1"
                          title="Modifica"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${raceType.id}/delete`}
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
      </div>
    );
  }
}

const mapStateToProps = ({ raceType }: IRootState) => ({
  raceTypeList: raceType.entities
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
)(RaceType);
