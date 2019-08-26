import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import GenericDeleteConfirmationDialog from '../../shared/component/generic-delete-confirmation-dialog';

import { INonAcsiTeam } from 'app/shared/model/non-acsi-team.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './non-acsi-team.reducer';

export interface INonAcsiTeamDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class NonAcsiTeamDeleteDialog extends React.Component<INonAcsiTeamDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.nonAcsiTeamEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { nonAcsiTeamEntity } = this.props;
    return (
      <GenericDeleteConfirmationDialog close={this.handleClose} confirm={this.confirmDelete} entityName="dell'associazione non affiliata" />
    );
  }
}

const mapStateToProps = ({ nonAcsiTeam }: IRootState) => ({
  nonAcsiTeamEntity: nonAcsiTeam.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NonAcsiTeamDeleteDialog);
