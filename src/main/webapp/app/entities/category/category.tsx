import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './category.reducer';
import { ICategory } from 'app/shared/model/category.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import NoElementFound from '../../shared/component/no-element-found';

export interface ICategoryProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ICategoryState {
  search: string;
}

export class Category extends React.Component<ICategoryProps, ICategoryState> {
  state: ICategoryState = {
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
    const { categoryList, match } = this.props;
    return (
      <div>
        <h2 id="category-heading" className="list-title">
          Categorie
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
          {categoryList && categoryList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>Alias</th>
                  <th className="text-center">Sesso</th>
                  <th className="text-center">Età Minima</th>
                  <th className="text-center">Età Massima</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {categoryList.map((category, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{category.name}</td>
                    <td>{category.alias}</td>
                    <td className="text-center">{category.gender}</td>
                    <td className="text-center">{category.minAge}</td>
                    <td className="text-center">{category.maxAge}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`${match.url}/${category.id}/edit`}
                          color="primary"
                          size="sm"
                          className="ml-1"
                          title="Modifica"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${category.id}/delete`}
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

/*
                        <Button tag={Link} to={`${match.url}/${category.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>

*/

const mapStateToProps = ({ category }: IRootState) => ({
  categoryList: category.entities
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
)(Category);
