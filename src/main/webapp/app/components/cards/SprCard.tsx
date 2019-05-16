import React from 'react';

import { Card, CardBody, CardFooter, Button, Container, CardTitle, Row, Col, Table } from 'reactstrap';
import { convertTerritory } from 'app/utils/conversions';

export const SprCountCard = ({ spr }) => (
  <Card className="card-stats mb-4 mb-xl-0">
    <CardBody className="text-center">
      <Row>
        <CardTitle
          tag="h5"
          className="text-uppercase text-muted mb-0">
          <strong className="text-danger">{convertTerritory(spr)}</strong>
        </CardTitle>
      </Row>
      <Row>
        <Col>
          <p className="h2 font-weight-bold mb-0">
            Priority:
            <strong className="text-danger">{spr.priority}</strong>
          </p>
          <p className="h2 font-weight-bold mb-0">
            Owner:
            <strong className="text-danger">{spr.user.login}</strong>
          </p>
        </Col>
      </Row>
    </CardBody>
  </Card>
);

export const SprReviewedCard = props => (
  <h1>hi</h1>
);

export const SprTable = ({ sprList }) => (
  <div className="table-responsive">
    <Table responsive>
      <thead>
      <tr>
        <th>SPR</th>
        <th>Priority</th>
        <th>User</th>
      </tr>
      </thead>
      <tbody>
      {sprList === undefined
        ? (
            <Container className="text-center">
              <h6>Hmm. Looks like there are no sprs.</h6>
            </Container>
        )
        : (sprList.map((spr, key) => (
            <tr key={`entity-${key}`}>
              <td>{spr.fullNumber}</td>
              <td>{spr.priority}</td>
              <td>{spr.user ? spr.user.login : ''}</td>
            </tr>
        )))
      }
      </tbody>
    </Table>
  </div>
);

export const TR = spr => (
  <tr key={`entity-${spr.id}`}>
    <td>{convertTerritory(spr)}</td>
    <td>{spr.priority}</td>
    <td>{spr.user ? spr.user.login : ''}</td>
  </tr>
);
