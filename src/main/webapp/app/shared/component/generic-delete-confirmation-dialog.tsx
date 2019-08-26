import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export interface IConfirmDialog {
  close: any;
  confirm: any;
  entityName: string;
}

const genericDeleteConfirmationDialog = (props: IConfirmDialog) => {
  const { close, confirm, entityName } = props;
  return (
    <Modal isOpen toggle={close}>
      <ModalHeader toggle={close}>Conferma Operazione</ModalHeader>
      <ModalBody id="acsiCyclingRacesApp.acsiTeam.delete.question">
        Sei sicuro di voler procedere con l'eliminazione {entityName}?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={close}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annulla
        </Button>
        <Button id="jhi-confirm-delete-acsiTeam" color="danger" onClick={confirm}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Elimina
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default genericDeleteConfirmationDialog;
