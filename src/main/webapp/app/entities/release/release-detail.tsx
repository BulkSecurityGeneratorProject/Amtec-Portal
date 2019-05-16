import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './release.reducer';
import { IRelease } from 'app/shared/model/release.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReleaseDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ReleaseDetail extends React.Component<IReleaseDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { releaseEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Release [<b>{releaseEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="date">Date</span>
            </dt>
            <dd>
              <TextFormat value={releaseEntity.date} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="territory">Territory</span>
            </dt>
            <dd>{releaseEntity.territory}</dd>
            <dt>
              <span id="build">Build</span>
            </dt>
            <dd>{releaseEntity.build}</dd>
            <dt>
              <span id="releaseLetter">Release Letter</span>
            </dt>
            <dd>{releaseEntity.releaseLetter}</dd>
            <dt>
              <span id="prefixLetter">Prefix Letter</span>
            </dt>
            <dd>{releaseEntity.prefixLetter}</dd>
            <dt>
              <span id="databaseVersion">Database Version</span>
            </dt>
            <dd>{releaseEntity.databaseVersion}</dd>
            <dt>
              <span id="wsVersion">Ws Version</span>
            </dt>
            <dd>{releaseEntity.wsVersion}</dd>
            <dt>
              <span id="tmaVersion">Tma Version</span>
            </dt>
            <dd>{releaseEntity.tmaVersion}</dd>
            <dt>
              <span id="port">Port</span>
            </dt>
            <dd>{releaseEntity.port}</dd>
            <dt>
              <span id="newFeatures">New Features</span>
            </dt>
            <dd>{releaseEntity.newFeatures}</dd>
            <dt>
              <span id="updatedFeatures">Updated Features</span>
            </dt>
            <dd>{releaseEntity.updatedFeatures}</dd>
            <dt>
              <span id="current">Current</span>
            </dt>
            <dd>{releaseEntity.current ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/entity/release" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/release/${releaseEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ release }: IRootState) => ({
  releaseEntity: release.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReleaseDetail);
