import './footer.scss';

import React from 'react';

import { Col, Row, Container } from 'reactstrap';

const Footer = props => (
  <footer className="footer page-content">
    <Container>
      <nav>
        <ul>
          <li>
            <a href="/register" >Register</a>
          </li>
        </ul>
      </nav>
    </Container>
  </footer>
);

export default Footer;
