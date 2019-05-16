import React from 'react';

import { NavItem, NavLink, Container, Row, Col, Card } from 'reactstrap';
import OpenSprCountCard from 'app/components/cards/OpenSprCountCard';
import ReviewedSprCountCard from 'app/components/cards/ReviewedSprCountCard';
import SprUserPercChart from 'app/components/charts/SprUserPercChart';
import CurrentReleaseCard from 'app/components/cards/CurrentReleaseCard';
import FutureReleaseCard from 'app/components/cards/FutureReleaseCard';

export const AuthenticatedView = ( ) => (
  <>
    <Container>
      <Row>
        <Col className="col-md-4">
          <h3 className="text-center">Current Releases</h3>
          <CurrentReleaseCard />
        </Col>
        <Col className="col-md-4">
          <h3 className="text-center">Upcoming Releases</h3>
          <FutureReleaseCard />
        </Col>
        <Col className="col-md-4">
          <h3 className="text-center">SPRs</h3>
          <OpenSprCountCard />
          <ReviewedSprCountCard />
        </Col>
      </Row>
      <hr/>
      <Row className="mt-5">
        <Col>
          <SprUserPercChart />
        </Col>
      </Row>
    </Container>
  </>
);

// export const AuthenticatedView = ( ) => (
//   <>
//     <Container className="text-center">
//       <Col>
//         <Row className="">
//           <h3>Current Releases</h3>
//           <CurrentReleaseCard />
//         </Row>
//         <Row className="">
//           <h3>Upcoming Releases</h3>
//           <FutureReleaseCard />
//         </Row>
//         <Row className="">
//           <h3>SPRs</h3>
//           <OpenSprCountCard />
//           <ReviewedSprCountCard />
//         </Row>
//       </Col>
//       <hr/>
//       <Row className="mt-5">
//         <Col>
//           <SprUserPercChart />
//         </Col>
//       </Row>
//     </Container>
//   </>
// );

export const UnAuthenticatedView = props => (
  <Container className="text-center">
    <Row>
      <Col className="col-md-6">
        <h1>You are not logged in</h1>
      </Col>
    </Row>
  </Container>
);
