import React from 'react';
import { makeStyles, createStyles, Theme } from '@material-ui/core/styles';
import { Chip } from '@material-ui/core';
import moment from 'moment';

interface ISubscriptionStatusChip {
  expirationDate: any;
}

const useStylesSubscriptionOpened = makeStyles((theme: Theme) =>
  createStyles({
    chip: {
      color: 'white',
      backgroundColor: 'green'
    }
  })
);

const useStylesSubscriptionClosed = makeStyles((theme: Theme) =>
  createStyles({
    chip: {
      color: 'white',
      backgroundColor: 'red'
    }
  })
);

const subscriptionStatusChip = (props: ISubscriptionStatusChip) => {
  let classes = useStylesSubscriptionClosed();
  let label = 'Iscrizioni Chiuse';

  if (moment().diff(props.expirationDate) < 0) {
    classes = useStylesSubscriptionOpened();
    label = 'Iscrizioni Aperte';
  }

  return <Chip label={label} className={classes.chip} />;
};

export default subscriptionStatusChip;
