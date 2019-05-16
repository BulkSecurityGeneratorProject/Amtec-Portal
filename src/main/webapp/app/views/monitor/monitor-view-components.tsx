import React from 'react';

import { NavItem, NavLink, Container, Row, Col, Card, CardGroup, CardTitle, CardText, CardFooter, CardBody, CardImg } from 'reactstrap';
import OpenSprCountCard from 'app/components/cards/OpenSprCountCard';
import ReviewedSprCountCard from 'app/components/cards/ReviewedSprCountCard';
import SprUserPercChart from 'app/components/charts/SprUserPercChart';
import CurrentReleaseCardCol from 'app/components/cards/CurrentReleaseCard';
import FutureReleaseCard from 'app/components/cards/FutureReleaseCard';

export const MonitorComponents = () => (
  <>
    <div className="container-fluid">
      <Row>
        {/* LEFT SIDE */}
        <Col className="col-md-7">
          <Row>
              <CurrentReleaseCardCol />
          </Row>;
          <Row>
            {/*<FutureReleaseCard/>*/}
          </Row>
        </Col>

        {/* RIGHT SIDE*/}
        <Col className="col-md-5">
          <SprUserPercChart/>
        </Col>
      </Row>
    </div>
  </>
);
