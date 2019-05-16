import React from 'react';

import { Card, CardBody, Row, Col, CardTitle } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';
import { connect } from 'react-redux';
import { getReviewedSprs } from 'app/entities/spr/spr.reducer';

export interface IReviewedSprCountCardProp extends StateProps, DispatchProps {
}

export interface IReviewedSprCountCardState {
  seconds: number;
}

export class ReviewedSprCountCard extends React.Component<IReviewedSprCountCardProp, IReviewedSprCountCardState> {
  private interval: number;

  state: IReviewedSprCountCardState = {
    seconds: 0
  };

  componentDidMount() {
    this.props.getReviewedSprs();
    this.interval = window.setInterval(() => this.tick(), 40000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  // We will refresh the components every 5 seconds
  tick() {
    this.setState(prevState => ({
      seconds: prevState.seconds + 1
    }));
    this.props.getReviewedSprs();
  }

  render() {
    const { reviewedSprEntities } = this.props;
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
                  Reviewed
                </CardTitle>
                <span className="h2 font-weight-bold mb-0">
                  {reviewedSprEntities.length}
                </span>
              </div>
              <Col className="col-auto">
                <div className="icon icon-shape bg-info text-white rounded-circle shadow">
                  <i className="fas fa-eye"/>
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
  reviewedSprEntities: spr.reviewedSprEntities
});

const mapDispatchToProps = {
  getReviewedSprs
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReviewedSprCountCard);
