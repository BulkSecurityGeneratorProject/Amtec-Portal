import React from 'react';

import { Card, CardBody, Row, Col, CardTitle } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';
import { connect } from 'react-redux';
import { getAllOpenSprs } from 'app/entities/spr/spr.reducer';

export interface ISOpenSprCountCardProp extends StateProps, DispatchProps {}

export interface IOpenSprCountCardState {
  seconds: number;
}

export class OpenSprCountCard extends React.Component<ISOpenSprCountCardProp, IOpenSprCountCardState> {
  private interval: number;

  state: IOpenSprCountCardState = {
    seconds: 0
  };

  componentDidMount() {
    this.props.getAllOpenSprs();
    this.interval = window.setInterval(() => this.tick(), 30000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  // We will refresh the components every 5 seconds
  tick() {
    this.setState(prevState => ({
      seconds: prevState.seconds + 1
    }));
    this.props.getAllOpenSprs();
  }

  render() {
    const { openSprEntites } = this.props;
    return (
      <>
        <Card className="card-stats mb-4 mb-xl-0">
          <CardBody>
            <Row>
              <div className="col">
                <CardTitle
                  tag="h5"
                  className="text-uppercase text-muted mb-0"
                >
                  Open Sprs
                </CardTitle>
                <span className="h2 font-weight-bold mb-0">
                  {openSprEntites.length}
                  </span>
              </div>
              <Col className="col-auto">
                <div className="icon icon-shape bg-danger text-white rounded-circle shadow">
                  <i className="fas fa-folder-open" />
                  {/*<FontAwesomeIcon icon="folder-open" />*/}
                </div>
              </Col>
            </Row>
          </CardBody>
        </Card>
      </>
    );
  }
}

const mapStateToProps = ({ spr }) => ({
  openSprEntites: spr.openSprEntites
});

const mapDispatchToProps = {
  getAllOpenSprs
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OpenSprCountCard);
