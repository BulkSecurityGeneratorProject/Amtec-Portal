import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './team-event.reducer';
import { ITeamEvent } from 'app/shared/model/team-event.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITeamEventUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITeamEventUpdateState {
  isNew: boolean;
}

export class TeamEventUpdate extends React.Component<ITeamEventUpdateProps, ITeamEventUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.start = convertDateTimeToServer(values.start);
    values.end = convertDateTimeToServer(values.end);

    if (errors.length === 0) {
      const { teamEventEntity } = this.props;
      const entity = {
        ...teamEventEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/team-event');
  };

  render() {
    const { teamEventEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    const { description } = teamEventEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="amtecPortalApp.teamEvent.home.createOrEditLabel">Create or edit a TeamEvent</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : teamEventEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="team-event-id">ID</Label>
                    <AvInput id="team-event-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="startLabel" for="team-event-start">
                    Start
                  </Label>
                  <AvInput
                    id="team-event-start"
                    type="datetime-local"
                    className="form-control"
                    name="start"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.teamEventEntity.start)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endLabel" for="team-event-end">
                    End
                  </Label>
                  <AvInput
                    id="team-event-end"
                    type="datetime-local"
                    className="form-control"
                    name="end"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.teamEventEntity.end)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="team-event-description">
                    Description
                  </Label>
                  <AvInput id="team-event-description" type="textarea" name="description" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/team-event" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  teamEventEntity: storeState.teamEvent.entity,
  loading: storeState.teamEvent.loading,
  updating: storeState.teamEvent.updating,
  updateSuccess: storeState.teamEvent.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeamEventUpdate);
