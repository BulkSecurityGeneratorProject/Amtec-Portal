import React from 'react';
import { connect } from 'react-redux';
import { Button, Row, Col, CardGroup, Card, Container, CardDeck } from 'reactstrap';

import { IRootState } from 'app/shared/reducers/index';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile, hideMenu, showMenu } from 'app/shared/reducers/application-profile';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { getCurrentReleases, getFutureReleases } from 'app/entities/release/release.reducer';
import SprUserPercChart from 'app/components/charts/SprUserPercChart';
import { ReleaseCard } from 'app/components/cards/ReleaseCard';
import OpenSprCountCard from 'app/components/cards/OpenSprCountCard';
import ReviewedSprCountCard from 'app/components/cards/ReviewedSprCountCard';
import { paginateEntities } from 'app/utils/conversions';

export interface IMonitorViewProps extends StateProps, DispatchProps {
}

export interface IMonitorViewState {
  buttonText: string;
  seconds: number;
}

const HIDE_MENU = 'HIDE MENU';
const SHOW_MENU = 'SHOW MENU';

export class MonitorView extends React.Component<IMonitorViewProps, IMonitorViewState> {
  private interval: number;

  constructor(props) {
    super(props);
    this.state = {
      buttonText: SHOW_MENU,
      seconds: 0
    };
  }

  componentDidMount() {
    this.props.getSession();
    this.props.hideMenu();
    this.props.getCurrentReleases();
    this.props.getFutureReleases();
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
    this.props.getFutureReleases();
  }

  onClick = event => {
    event.preventDefault();

    if (this.state.buttonText === HIDE_MENU) {
      this.setState({ buttonText: SHOW_MENU });
      this.props.hideMenu();
    } else {
      this.setState({ buttonText: HIDE_MENU });
      this.props.showMenu();
    }
  };

  render() {
    const { currentEntities, futureEntities } = this.props;
    const backgroundColor = '#32325d';
    return (
      <>
        <div className="container-fluid" style={{ backgroundColor }}>
          <Row>
            {/* LEFT SIDE */}
            <Col className="col-md-7">
              <Row>
                {/*<CardGroup>*/}
                  {currentEntities.entities === undefined
                    ? (
                      <CardGroup>
                      <Card className="card-stats mb-4 mb-xl-0">
                        <Container className="text-center">
                          <h6>Hmm. Looks like there are no current releases scheduled.</h6>
                        </Container>
                      </Card>
                      </CardGroup>
                    )
                    : (currentEntities.entities.map(release => (
                      <ReleaseCard type={'CURRENT'} releaseType={release} />
                    )))
                  }
                {/*</CardGroup>*/}
              </Row>
              <hr/>
              <Row>
                {/*<CardGroup>*/}
                {futureEntities.entities === undefined
                  ? (
                    <Card>
                      <Container className="text-center">
                        <h6>Hmm. Looks like there are no future releases scheduled.</h6>
                      </Container>
                    </Card>
                  )
                  : (futureEntities.entities.map(release => (
                    <ReleaseCard type={'FUTURE'} releaseType={release}/>
                  )))
                }
                {/*</CardGroup>*/}
              </Row>
            </Col>

            {/* RIGHT SIDE*/}
            <Col className="col-md-5">
              <CardDeck>
                <OpenSprCountCard/>
                <ReviewedSprCountCard/>
              </CardDeck>
              <br/>
              <SprUserPercChart/>
            </Col>
          </Row>
          <hr/>
          <Row>
            <Col className="text-center">
              <Button
                className="btn"
                onClick={this.onClick}>{this.state.buttonText}
              </Button>
            </Col>
          </Row>
        </div>
      </>
    );
  }
}

const mapStateToProps = ({ authentication, applicationProfile, release }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN]),
  ribbonEnv: applicationProfile.ribbonEnv,
  isInProduction: applicationProfile.inProduction,
  isSwaggerEnabled: applicationProfile.isSwaggerEnabled,
  menuHidden: applicationProfile.menuHidden,
  currentEntities: release.currentEntities,
  futureEntities: release.futureEntities
});

const mapDispatchToProps = { getSession, getProfile, hideMenu, showMenu, getCurrentReleases, getFutureReleases };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MonitorView);
