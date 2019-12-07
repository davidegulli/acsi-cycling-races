import React from 'react';
import { Card, CardHeader, CardContent, Typography } from '@material-ui/core';
import { Row, Col } from 'reactstrap';
import { AvRadio } from 'availity-reactstrap-validation';

interface ISubscriptionType {
  id: string;
  price: string;
  name: string;
  description?: boolean;
  rules?: boolean;
}

const subscriptionType = (props: ISubscriptionType) => (
  <Card>
    <CardHeader title={props.name} />
    <CardContent>
      <Row>
        <Col md="4">
          <AvRadio label="" value={props.id} />
        </Col>
        <Col>
          <Typography variant="h4" component="p">
            {props.price} €
          </Typography>
        </Col>
      </Row>
      <Row>
        <Col>
          <Typography variant="body2" color="textSecondary" component="p" className="mt-3">
            {props.description}
          </Typography>
        </Col>
      </Row>
    </CardContent>
  </Card>
);

export default subscriptionType;
