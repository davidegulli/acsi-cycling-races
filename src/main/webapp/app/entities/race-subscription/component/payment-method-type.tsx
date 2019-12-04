import React from 'react';
import { Row, Col } from 'reactstrap';
import { AvRadio } from 'availity-reactstrap-validation';
import { Card, CardContent, Typography } from '@material-ui/core';

interface IPaymentMethodType {
  label: string;
  value: string;
  imgUrl: string;
  selected?: boolean;
}

const paymentMethodType = (props: IPaymentMethodType) => (
  <Card>
    <CardContent>
      <Row>
        <Col md="4">
          <AvRadio value={props.value} />
        </Col>
        <Col>
          <img src={props.imgUrl} className="payment-method-image" />
        </Col>
      </Row>
      <Row>
        <Col>
          <Typography variant="h6" component="p" className="text-center mt-3">
            {props.label}
          </Typography>
        </Col>
      </Row>
    </CardContent>
  </Card>
);

export default paymentMethodType;

/*
    <div className="payment-method-radio-section">
    <Row>
      <Col>
        <div className="payment-method-radio">
          <AvRadio label={props.label} value={props.value}/>
        </div>
      </Col>
      <Col>
        <img src={props.imgUrl} className="payment-method-image" />
      </Col>
    </Row>
  </div>
*/
