import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Label, Row } from 'reactstrap';
import { AvFeedback, AvField, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { setFileData } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getReleases } from 'app/entities/release/release.reducer';
import { createEntity, getEntity, reset, setBlob, updateEntity } from './spr.reducer';
import { Territory } from 'app/shared/model/spr.model';

// tslint:disable-next-line:no-unused-variable

export interface ISprUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISprUpdateState {
  isNew: boolean;
  userId: string;
  releaseId: string;
  sprTerr: string;
  sprNum: string;
}

export class SprUpdate extends React.Component<ISprUpdateProps, ISprUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      releaseId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id,
      sprTerr: '',
      sprNum: ''
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

    this.props.getUsers();
    this.props.getReleases();

    const hasValue = this.props.sprEntity.fullNumber !== undefined;

    if (!hasValue) {
      if (!this.props.loading) {
        switch (this.props.sprEntity.territory) {
          case Territory.CETC_WEST:
            this.setSprTerr('CW-');
            break;
          case Territory.HUDSON:
            this.setSprTerr('AH-');
            break;
          case Territory.NED:
            this.setSprTerr('NED-');
            break;
          case Territory.PSCC:
            this.setSprTerr('PS-');
            break;
          case Territory.SED:
            this.setSprTerr('SED-');
            break;
          default:
            this.setSprTerr('UN-');
            break;
        }
      }
    } else {
      this.setSprTerr(this.props.sprEntity.fullNumber);
    }
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { sprEntity } = this.props;
      const entity = {
        ...sprEntity,
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
    this.props.history.push('/entity/spr');
  };

  onChange = event => {
    this.setState({
      sprNum: event.target.value
    });
  };

  changeSprTerr = event => {
    switch (event.target.value) {
      case Territory.CETC_WEST:
        this.setSprTerr('CW-');
        break;
      case Territory.HUDSON:
        this.setSprTerr('AH-');
        break;
      case Territory.NED:
        this.setSprTerr('NED-');
        break;
      case Territory.PSCC:
        this.setSprTerr('PS-');
        break;
      case Territory.SED:
        this.setSprTerr('SED-');
        break;
      default:
        this.setSprTerr('UN-');
        break;
    }
  };

  setSprTerr = val => {
    this.setState({
      sprTerr: val
    });
  };

  render() {
    const { sprEntity, users, releases, loading, updating } = this.props;
    const { isNew, sprNum, sprTerr } = this.state;

    const { description } = sprEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="amtecPortalApp.spr.home.createOrEditLabel">Create or edit a Spr</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : sprEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="spr-id">ID</Label>
                    <AvInput id="spr-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="territoryLabel" for="spr-territory">
                    Territory
                  </Label>
                  <AvInput
                    id="spr-territory"
                    type="select"
                    className="form-control"
                    name="territory"
                    onChange={this.changeSprTerr}
                    value={(!isNew && sprEntity.territory) || 'CETC_WEST'}
                  >
                    <option value="CETC_WEST">CETC WEST</option>
                    <option value="HUDSON">HUDSON</option>
                    <option value="SED">SED</option>
                    <option value="NED">NED</option>
                    <option value="CHICAGO">CHICAGO</option>
                    <option value="PSCC">PSCC</option>
                    <option value="UNIFIED">UNIFIED</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="numberLabel" for="spr-number">
                    Number
                  </Label>
                  <AvField
                    id="spr-number"
                    type="string"
                    className="form-control"
                    name="number"
                    onChange={this.onChange}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="fullNumberLabel" for="spr-fullNumber">
                    Full Number
                  </Label>
                  <AvField
                    id="spr-fullNumber"
                    type="text"
                    name="fullNumber"
                    value={sprTerr + sprNum}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="spr-description">
                    Description
                  </Label>
                  <AvInput id="spr-description" type="textarea" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="priorityLabel" for="spr-priority">
                    Priority
                  </Label>
                  <AvInput
                    id="spr-priority"
                    type="select"
                    className="form-control"
                    name="priority"
                    value={(!isNew && sprEntity.priority) || 'SHOWSTOPPER'}
                  >
                    <option value="SHOWSTOPPER">SHOWSTOPPER</option>
                    <option value="EMERGENCY">EMERGENCY</option>
                    <option value="HIGH">HIGH</option>
                    <option value="LOW">LOW</option>
                    <option value="ROUTINE">ROUTINE</option>
                    <option value="DEFER">DEFER</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="resolutionLabel" for="spr-resolution">
                    Resolution
                  </Label>
                  <AvInput
                    id="spr-resolution"
                    type="select"
                    className="form-control"
                    name="resolution"
                    value={(!isNew && sprEntity.resolution) || 'NEW'}
                  >
                    <option value="NEW">NEW</option>
                    <option value="RELEASED">RELEASED</option>
                    <option value="DUPLICATE">DUPLICATE</option>
                    <option value="TESTED">TESTED</option>
                    <option value="FIXED">FIXED</option>
                    <option value="PARTIALLY_FIXED">PARTIALLY_FIXED</option>
                    <option value="REVIEWED">REVIEWED</option>
                    <option value="CANNOT_REPRODUCE">CANNOT_REPRODUCE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="jiraLinkLabel" for="spr-jiraLink">
                    Jira Link
                  </Label>
                  <AvField id="spr-jiraLink" type="text" name="jiraLink" />
                </AvGroup>
                {/*<AvGroup>*/}
                  {/*<Label id="reviewerIdLabel" for="spr-reviewerId">*/}
                    {/*Reviewer Id*/}
                  {/*</Label>*/}
                  {/*<AvField id="spr-reviewerId" type="string" className="form-control" name="reviewerId" />*/}
                {/*</AvGroup>*/}
                <AvGroup>
                  <Label for="reviewerIdLabel">Reviewer</Label>
                  <AvInput id="spr-reviewerId" type="select" className="form-control" name="reviewerId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.login}
                        </option>
                      ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="spr-user">Owner</Label>
                  <AvInput id="spr-user" type="select" className="form-control" name="user.id">
                    <option value="" key="0"/>
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="spr-release">Release</Label>
                  <AvInput id="spr-release" type="select" className="form-control" name="release.id">
                    <option value="" key="0" />
                    {releases
                      ? releases.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/spr" replace color="info">
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
  users: storeState.userManagement.users,
  releases: storeState.release.entities,
  sprEntity: storeState.spr.entity,
  loading: storeState.spr.loading,
  updating: storeState.spr.updating,
  updateSuccess: storeState.spr.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getReleases,
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
)(SprUpdate);
