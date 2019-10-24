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

interface ISubscriptionType {
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

const subscriptionType = (props: ISubscriptionType) => {
  const classes = useStyles();
  const [open, setOpen] = React.useState(false);
  const dialogForm = React.useRef(null);

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
        <span className="pt-5 section-title">Tipologie di Iscrizione</span>
        {props.rows !== undefined && props.rows.length < 3 ? (
          <Button color="primary" className="input-group-addon float-right mr-2" onClick={handleClickOpen}>
            <FontAwesomeIcon icon="plus" />
          </Button>
        ) : null}
      </div>
      <Paper className={classes.root}>
        <Table className={classes.table}>
          <TableHead>
            <TableRow>
              <TableCell>Tipo di Iscrizione</TableCell>
              <TableCell>Descrizione</TableCell>
              <TableCell>Regolamento</TableCell>
              <TableCell>Costo</TableCell>
              <TableCell />
            </TableRow>
          </TableHead>
          <TableBody>
            {props.rows !== undefined
              ? props.rows.map((row, index) => (
                  <TableRow key={index} className="align-top">
                    <TableCell>{row.name}</TableCell>
                    <TableCell>{row.description}</TableCell>
                    <TableCell>{row.rules}</TableCell>
                    <TableCell align="right">{row.price}</TableCell>
                    <TableCell>
                      <Button className="input-group-addon float-right mr-2" id={index} onClick={removeRow}>
                        <FontAwesomeIcon icon="trash" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))
              : null}
          </TableBody>
        </Table>
        {props.rows === undefined || props.rows.length === 0 ? (
          <div className="text-center pt-3">
            <p className="text-muted">Ancora non è stata inserita nessuna tipologia di iscrizione</p>
          </div>
        ) : null}
      </Paper>
      <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
        <DialogTitle id="form-dialog-title">Tipologia di Iscrizione</DialogTitle>
        <AvForm id="subscriptionTypeForm" onSubmit={dialogFormSubmit} ref={dialogForm}>
          <DialogContent>
            <DialogContentText>
              Per inserire una nuova tipologia di iscrizione con il relativo costo compila il seguente form:
            </DialogContentText>
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
              <Label id="rulesLabel" for="subscriptionType-rules">
                Regole
              </Label>
              <AvField
                id="subscriptionType-rules"
                type="text"
                name="rules"
                validate={{
                  minLength: { value: 5, errorMessage: 'Il campo deve esse composto da almeno 5 caratteri' },
                  maxLength: { value: 200, errorMessage: 'Il campo può essere composto da un massimo di 200 caratteri' }
                }}
              />
            </AvGroup>
            <AvGroup>
              <Label id="priceLabel" for="subscriptionType-price">
                Costo
              </Label>
              <AvField
                id="subscriptionType-price"
                type="text"
                name="price"
                validate={{
                  required: { value: true, errorMessage: 'Il campo è obbligatorio' },
                  number: { value: true, errorMessage: 'Inserire una cifra valida' }
                }}
              />
            </AvGroup>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleClose}>Cancel</Button>
            <Button onClick={insertData} form="subscriptionTypeForm" color="primary">
              Subscribe
            </Button>
          </DialogActions>
        </AvForm>
      </Dialog>
    </Fragment>
  );
};

export default subscriptionType;
// onClick={() => removeRow(index)}
