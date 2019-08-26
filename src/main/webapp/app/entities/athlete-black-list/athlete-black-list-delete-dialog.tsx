import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IAthleteBlackList } from 'app/shared/model/athlete-black-list.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './athlete-black-list.reducer';
import GenericDeleteConfirmation from '../../shared/component/generic-delete-confirmation-dialog';

export interface IAthleteBlackListDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AthleteBlackListDeleteDialog extends React.Component<IAthleteBlackListDeleteDialogProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  confirmDelete = event => {
    this.props.deleteEntity(this.props.athleteBlackListEntity.id);
    this.handleClose(event);
  };

  handleClose = event => {
    event.stopPropagation();
    this.props.history.goBack();
  };

  render() {
    const { athleteBlackListEntity } = this.props;
    return <GenericDeleteConfirmation close={this.handleClose} confirm={this.confirmDelete} entityName="dell'atleta dalla blacklist" />;
  }
}

const mapStateToProps = ({ athleteBlackList }: IRootState) => ({
  athleteBlackListEntity: athleteBlackList.entity
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AthleteBlackListDeleteDialog);
