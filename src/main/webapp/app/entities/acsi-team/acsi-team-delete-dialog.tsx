import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import GenericDeleteConfirmationDialog from '../../shared/component/generic-delete-confirmation-dialog';

import { IAcsiTeam } from 'app/shared/model/acsi-team.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './acsi-team.reducer';

export interface IAcsiTeamDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AcsiTeamDeleteDialog extends React.Component<IAcsiTeamDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.acsiTeamEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { acsiTeamEntity } = this.props;
    return (
      <GenericDeleteConfirmationDialog close={this.handleClose} confirm={this.confirmDelete} entityName="dell'associazione affiliata" />
    );
  }
}

const mapStateToProps = ({ acsiTeam }: IRootState) => ({
  acsiTeamEntity: acsiTeam.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AcsiTeamDeleteDialog);
