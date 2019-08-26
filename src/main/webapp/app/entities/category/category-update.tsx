import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './category.reducer';
import { ICategory } from 'app/shared/model/category.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICategoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICategoryUpdateState {
  isNew: boolean;
}

export class CategoryUpdate extends React.Component<ICategoryUpdateProps, ICategoryUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { categoryEntity } = this.props;
      const entity = {
        ...categoryEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/category');
  };

  render() {
    const { categoryEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="acsiCyclingRacesApp.category.home.createOrEditLabel" className="sheet-title">
              Categoria
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : categoryEntity} onSubmit={this.saveEntity}>
                <AvGroup>
                  <Label id="nameLabel" for="category-name">
                    Nome
                  </Label>
                  <AvField
                    id="category-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="aliasLabel" for="category-alias">
                    Alias
                  </Label>
                  <AvField id="category-alias" type="text" name="alias" />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel" for="category-gender">
                    Sesso
                  </Label>
                  <AvInput
                    id="category-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && categoryEntity.gender) || 'MALE'}
                  >
                    <option value="MALE">UOMINI</option>
                    <option value="FEMALE">DONNE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="minAgeLabel" for="category-minAge">
                    Età Minima
                  </Label>
                  <AvField
                    id="category-minAge"
                    type="string"
                    className="form-control"
                    name="minAge"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="maxAgeLabel" for="category-maxAge">
                    Età Massima
                  </Label>
                  <AvField
                    id="category-maxAge"
                    type="string"
                    className="form-control"
                    name="maxAge"
                    validate={{
                      required: { value: true, errorMessage: 'Il campo è obbligatorio' }
                    }}
                  />
                </AvGroup>
                <div className="form-button-holder">
                  <Button tag={Link} id="cancel-save" to="/entity/category" replace>
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">Indietro</span>
                  </Button>
                  &nbsp;
                  <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp; Salva
                  </Button>
                </div>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  categoryEntity: storeState.category.entity,
  loading: storeState.category.loading,
  updating: storeState.category.updating,
  updateSuccess: storeState.category.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CategoryUpdate);
