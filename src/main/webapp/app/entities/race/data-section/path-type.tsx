import React, { Fragment } from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  createStyles,
  Theme,
  makeStyles
} from '@material-ui/core';
import { Button, Label } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';

interface IPathType {
  rows: any;
  addRowHandler: any;
  removeRowHandler: any;
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      width: '100%',
      marginTop: theme.spacing(3),
      overflowX: 'auto'
    },
    table: {
      minWidth: 650
    }
  })
);

const pathType = (props: IPathType) => {
  const classes = useStyles();
  const [open, setOpen] = React.useState(false);
  const dialogForm = React.useRef(null);
  let rows = [];

  if (props.rows !== undefined) {
    rows = [...props.rows];
  }

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const dialogFormSubmit = (event, errors, values) => {
    if (errors.length === 0) {
      props.addRowHandler(values);

      handleClose();
    }
  };

  const insertData = () => {
    dialogForm.current.submit();
  };

  const removeRow = event => {
    props.removeRowHandler(event.target.id);
  };

  return (
    <Fragment>
      <div className="mt-5">
        <span className="pt-5 section-title">Tipologie di Percorso</span>
        {rows.length < 3 ? (
          <Button color="primary" className="input-group-addon float-right mr-2" onClick={handleClickOpen}>
            <FontAwesomeIcon icon="plus" />
          </Button>
        ) : null}
      </div>
      <Paper className={classes.root}>
        <Table className={classes.table}>
          <TableHead>
            <TableRow>
              <TableCell>Tipo di Percorso</TableCell>
              <TableCell>Descrizione</TableCell>
              <TableCell>Distanza</TableCell>
              <TableCell />
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row, index) => (
              <TableRow key={index} className="align-top">
                <TableCell>{row.name}</TableCell>
                <TableCell>{row.description}</TableCell>
                <TableCell align="right">{row.distance}</TableCell>
                <TableCell>
                  <Button className="input-group-addon float-right mr-2" id={index} onClick={removeRow}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        {rows.length === 0 ? (
          <div className="text-center pt-3">
            <p className="text-muted">Ancora non è stata inserita nessuna tipologia di percorso</p>
          </div>
        ) : null}
      </Paper>
      <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
        <DialogTitle id="form-dialog-title">Tipologia di Percorso</DialogTitle>
        <AvForm id="pathTypeForm" onSubmit={dialogFormSubmit} ref={dialogForm}>
          <DialogContent>
            <DialogContentText>Per inserire una nuova tipologia di percorso compila il seguente form:</DialogContentText>
            <AvGroup>
              <Label id="nameLabel" for="subscriptionType-name">
                Nome
              </Label>
              <AvField
                id="subscriptionType-name"
                type="text"
                name="name"
                validate={{
                  required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                  minLength: { value: 5, errorMessage: 'Il campo deve esse composto da almeno 5 caratteri' },
                  maxLength: { value: 30, errorMessage: 'Il campo può essere composto da un massimo di 30 caratteri' }
                }}
              />
            </AvGroup>
            <AvGroup>
              <Label id="descriptionLabel" for="subscriptionType-description">
                Descrizione
              </Label>
              <AvField
                id="subscriptionType-description"
                type="text"
                name="description"
                validate={{
                  required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                  minLength: { value: 5, errorMessage: 'Il campo deve esse composto da almeno 5 caratteri' },
                  maxLength: { value: 200, errorMessage: 'Il campo può essere composto da un massimo di 200 caratteri' }
                }}
              />
            </AvGroup>
            <AvGroup>
              <Label id="distanceLabel" for="subscriptionType-distance">
                Distanza
              </Label>
              <AvField
                id="subscriptionType-distance"
                type="text"
                name="distance"
                validate={{
                  required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                  number: { value: true, errorMessage: 'Inserire una cifra valida' }
                }}
              />
            </AvGroup>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose}>Cancel</Button>
            <Button onClick={insertData} form="pathTypeForm" color="primary">
              Subscribe
            </Button>
          </DialogActions>
        </AvForm>
      </Dialog>
    </Fragment>
  );
};

export default pathType;
