import React from 'react';

import { Card, CardBody, CardFooter, Button, Container, CardTitle, Row, Col } from 'reactstrap';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, APP_MM_DD_YYYY_FORMAT } from 'app/config/constants';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';
import { connect } from 'react-redux';
import { getSession } from 'app/shared/reducers/authentication';
import { getCurrentReleases } from 'app/entities/release/release.reducer';

export interface ICurrentReleaseProp extends StateProps, DispatchProps {}

export interface ICurrentReleaseState {
  seconds: number;
}

export class CurrentReleaseCard extends React.Component<ICurrentReleaseProp, ICurrentReleaseState> {
  private interval: number;

  state: ICurrentReleaseState = {
    seconds: 0
  };
  componentDidMount() {
    this.props.getSession();
    this.props.getCurrentReleases();
    this.interval = window.setInterval(() => this.tick(), 10000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  // We will refresh the components every 5 seconds
  tick() {
    this.setState(prevState => ({
      seconds: prevState.seconds + 1
    }));
    this.props.getCurrentReleases();
  }
  render() {
    const { account, currentEntities } = this.props;
    return (
      <div>
      {currentEntities.entities === undefined
        ? (
          <Card className="card-stats mb-4 mb-xl-0">
            <Container className="text-center">
              <h6>Hmm. Looks like there are no current releases scheduled.</h6>
            </Container>
          </Card>
        )
        : (currentEntities.entities.map((release, key) => (
          <Card className="card-stats mb-4 mb-xl-0" key={key}>
            <CardBody className="">
              <Row>
                <CardTitle
                  tag="h5"
                  className="text-uppercase text-muted mb-0">
                  Current Release: <strong className="text-danger">{release.build}</strong>
                </CardTitle>
              </Row>
              <Row>
                <Col>
                  <Row>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-left">
                        Date:
                      </p>
                    </Col>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-right">
                        <strong className="text-danger">
                          <TextFormat value={release.date} type="date" format={APP_MM_DD_YYYY_FORMAT} />
                        </strong>
                      </p>
                    </Col>
                  </Row>
                  <Row>
                    <Col className="justify-content-start">
                      <p className="h5 font-weight-bold mb-0 ">
                        Territory:
                      </p>
                    </Col>
                    <Col>
                      <p className="h5 font-weight-bold mb-0">
                        <strong className="text-danger">{release.territory}</strong>
                      </p>
                    </Col>
                  </Row>

                  <Row>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-left">
                        Release Letter:
                      </p>
                    </Col>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-right">
                        <strong className="text-danger">{release.releaseLetter}</strong>
                      </p>
                    </Col>
                  </Row>

                  <Row>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-left">
                        Release Prefix:
                      </p>
                    </Col>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-right">
                        <strong className="text-danger">{release.prefixLetter}</strong>
                      </p>
                    </Col>
                  </Row>

                  <Row>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-left">
                        Database Version:
                      </p>
                    </Col>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-right">
                        <strong className="text-danger">{release.databaseVersion}</strong>
                      </p>
                    </Col>
                  </Row>

                  <Row>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-left">
                        Workstation Version:
                      </p>
                    </Col>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-right">
                        <strong className="text-danger">{release.wsVersion}</strong>
                      </p>
                    </Col>
                  </Row>

                  <Row>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-left">
                        TMA Version:
                      </p>
                    </Col>
                    <Col>
                      <p className="h5 font-weight-bold mb-0 pull-right">
                        <strong className="text-danger">{release.tmaVersion}</strong>
                      </p>
                    </Col>
                  </Row>

                </Col>
              </Row>
            </CardBody>
          </Card>
        )))
      }
      </div>
    );
  }
}

const mapStateToProps = ({ authentication, release }) => ({
  account: authentication.account,
  isAuthenticated: authentication.isAuthenticated,
  currentEntities: release.currentEntities
});

const mapDispatchToProps = {
  getSession,
  getCurrentReleases
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CurrentReleaseCard);
