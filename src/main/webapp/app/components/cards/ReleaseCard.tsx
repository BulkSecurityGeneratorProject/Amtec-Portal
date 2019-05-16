import { TextFormat } from 'react-jhipster';
import { APP_MM_DD_YYYY_FORMAT } from 'app/config/constants';
import React from 'react';
import { Card, CardBody, CardFooter, Button, Container, CardTitle, Row, Col } from 'reactstrap';

export const ReleaseCard = ({ type, releaseType }) => (
  <Card key={releaseType.id} className="mb-4 mb-xl-0">
    <CardBody>
      <Row>
        <CardTitle
          tag="h5"
          className="text-uppercase text-muted mb-0 text-nowrap">
          {type} Release: <strong className="text-danger">{releaseType.build}</strong>
        </CardTitle>
      </Row>
    </CardBody>
    <table className="table table-borderless table-sm">
      <tbody>
      <tr>
        <td style={{ textAlign: 'left' }}>Date:</td>
        <td>
          <strong className="text-danger">
            <TextFormat value={releaseType.date} type="date" format={APP_MM_DD_YYYY_FORMAT}/>
          </strong>
        </td>
      </tr>
      <tr>
        <td>Territory:</td>
        <td><p className="h5 font-weight-bold mb-0 text-nowrap">
          <strong className="text-danger">{releaseType.territory}</strong>
        </p></td>
      </tr>
      <tr>
        <td>Release Letter:</td>
        <td>
          <p className="h5 font-weight-bold mb-0 pull-right text-nowrap">
            <strong className="text-danger">{releaseType.releaseLetter}</strong>
          </p>
        </td>
      </tr>
      <tr>
        <td>Release Prefix:</td>
        <td>
          <p className="h5 font-weight-bold mb-0 pull-right text-nowrap">
            <strong className="text-danger">{releaseType.prefixLetter}</strong>
          </p>
        </td>
      </tr>
      <tr>
        <td>Database Version:</td>
        <td>
          <p className="h5 font-weight-bold mb-0 pull-right text-nowrap">
            <strong className="text-danger">{releaseType.databaseVersion}</strong>
          </p>
        </td>
      </tr>
      <tr>
        <td>Workstation Version:</td>
        <td>
          <p className="h5 font-weight-bold mb-0 pull-right text-nowrap">
            <strong className="text-danger">{releaseType.wsVersion}</strong>
          </p>
        </td>
      </tr>
      <tr>
        <td>TMA Version:</td>
        <td>
          <p className="h5 font-weight-bold mb-0 pull-right text-nowrap">
            <strong className="text-danger">{releaseType.tmaVersion}</strong>
          </p>
        </td>
      </tr>
      </tbody>
    </table>
  </Card>
);
