import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './team-event.reducer';
import { ITeamEvent } from 'app/shared/model/team-event.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITeamEventDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TeamEventDetail extends React.Component<ITeamEventDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { teamEventEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            TeamEvent [<b>{teamEventEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="start">Start</span>
            </dt>
            <dd>
              <TextFormat value={teamEventEntity.start} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="end">End</span>
            </dt>
            <dd>
              <TextFormat value={teamEventEntity.end} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{teamEventEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/team-event" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/team-event/${teamEventEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ teamEvent }: IRootState) => ({
  teamEventEntity: teamEvent.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeamEventDetail);
