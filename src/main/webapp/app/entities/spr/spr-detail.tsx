import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './spr.reducer';
import { ISpr } from 'app/shared/model/spr.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISprDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SprDetail extends React.Component<ISprDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { sprEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Spr [<b>{sprEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="territory">Territory</span>
            </dt>
            <dd>{sprEntity.territory}</dd>
            <dt>
              <span id="number">Number</span>
            </dt>
            <dd>{sprEntity.number}</dd>
            <dt>
              <span id="fullNumber">Full Number</span>
            </dt>
            <dd>{sprEntity.fullNumber}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{sprEntity.description}</dd>
            <dt>
              <span id="priority">Priority</span>
            </dt>
            <dd>{sprEntity.priority}</dd>
            <dt>
              <span id="resolution">Resolution</span>
            </dt>
            <dd>{sprEntity.resolution}</dd>
            <dt>
              <span id="jiraLink">Jira Link</span>
            </dt>
            <dd>{sprEntity.jiraLink}</dd>
            <dt>
              <span id="reviewerId">Reviewer Id</span>
            </dt>
            <dd>{sprEntity.reviewerId}</dd>
            <dt>User</dt>
            <dd>{sprEntity.user ? sprEntity.user.login : ''}</dd>
            <dt>Release</dt>
            <dd>{sprEntity.release ? sprEntity.release.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/spr" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/spr/${sprEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ spr }: IRootState) => ({
  sprEntity: spr.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SprDetail);
