import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import GenericDeleteConfirmationDialog from '../../shared/component/generic-delete-confirmation-dialog';

import { IRaceType } from 'app/shared/model/race-type.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './race-type.reducer';

export interface IRaceTypeDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RaceTypeDeleteDialog extends React.Component<IRaceTypeDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.raceTypeEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    return <GenericDeleteConfirmationDialog close={this.handleClose} confirm={this.confirmDelete} entityName="della disciplina" />;
  }
}

const mapStateToProps = ({ raceType }: IRootState) => ({
  raceTypeEntity: raceType.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RaceTypeDeleteDialog);
