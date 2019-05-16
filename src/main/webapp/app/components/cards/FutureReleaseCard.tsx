import React from 'react';

import { Card, CardBody, CardFooter, Button, Container, CardTitle, Row, Col } from 'reactstrap';
import { APP_MM_DD_YYYY_FORMAT } from 'app/config/constants';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';
import { connect } from 'react-redux';
import { getSession } from 'app/shared/reducers/authentication';
import { getFutureReleases } from 'app/entities/release/release.reducer';

export interface IFutureReleaseProp extends StateProps, DispatchProps {}

export interface IFutureReleaseState {
  seconds: number;
}

export class FutureReleaseCard extends React.Component<IFutureReleaseProp, IFutureReleaseState> {
  private interval: number;

  state: IFutureReleaseState = {
    seconds: 0
  };
  componentDidMount() {
    this.props.getSession();
    this.props.getFutureReleases();
    this.interval = window.setInterval(() => this.tick(), 20000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  // We will refresh the components every 5 seconds
  tick() {
    this.setState(prevState => ({
      seconds: prevState.seconds + 1
    }));
    this.props.getFutureReleases();
  }
  render() {
    const { account, futureEntities } = this.props;
    return (
      <div>
        {futureEntities.entities === undefined
          ? (
            <Card>
              <Container className="text-center">
                <h6>Hmm. Looks like there are no future releases scheduled.</h6>
              </Container>
            </Card>
          )
          : (futureEntities.entities.map((release, key) => (
            <Card className="card-stats mb-4 mb-xl-0" key={key}>
              <CardBody>
                <Row>
                  <CardTitle
                    tag="h5"
                    className="text-uppercase text-muted mb-0">
                    Future Release: <strong className="text-danger">{release.build}</strong>
                  </CardTitle>
                </Row>
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
                  <Col>
                    <Row>
                      <Col>
                        <p className="h5 font-weight-bold mb-0 pull-left">
                          Territory:
                        </p>
                      </Col>
                      <Col>
                        <p className="h5 font-weight-bold mb-0 pull-right">
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
  futureEntities: release.futureEntities
});

const mapDispatchToProps = {
  getSession,
  getFutureReleases
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FutureReleaseCard);
