import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './release.reducer';
import { IRelease } from 'app/shared/model/release.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IReleaseUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IReleaseUpdateState {
  isNew: boolean;
}

export class ReleaseUpdate extends React.Component<IReleaseUpdateProps, IReleaseUpdateState> {
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
    values.date = convertDateTimeToServer(values.date);

    if (errors.length === 0) {
      const { releaseEntity } = this.props;
      const entity = {
        ...releaseEntity,
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
    this.props.history.push('/entity/release');
  };

  render() {
    const { releaseEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    const { newFeatures, updatedFeatures } = releaseEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="amtecPortalApp.release.home.createOrEditLabel">Create or edit a Release</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : releaseEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="release-id">ID</Label>
                    <AvInput id="release-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dateLabel" for="release-date">
                    Date
                  </Label>
                  <AvInput
                    id="release-date"
                    type="datetime-local"
                    className="form-control"
                    name="date"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.releaseEntity.date)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="territoryLabel" for="release-territory">
                    Territory
                  </Label>
                  <AvInput
                    id="release-territory"
                    type="select"
                    className="form-control"
                    name="territory"
                    value={(!isNew && releaseEntity.territory) || 'CETC_WEST'}
                  >
                    <option value="CETC_WEST">CETC_WEST</option>
                    <option value="HUDSON">HUDSON</option>
                    <option value="SED">SED</option>
                    <option value="NED">NED</option>
                    <option value="CHICAGO">CHICAGO</option>
                    <option value="PSCC">PSCC</option>
                    <option value="UNIFIED">UNIFIED</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="buildLabel" for="release-build">
                    Build
                  </Label>
                  <AvField
                    id="release-build"
                    type="string"
                    className="form-control"
                    name="build"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="releaseLetterLabel" for="release-releaseLetter">
                    Release Letter
                  </Label>
                  <AvField
                    id="release-releaseLetter"
                    type="text"
                    name="releaseLetter"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="prefixLetterLabel" for="release-prefixLetter">
                    Prefix Letter
                  </Label>
                  <AvField id="release-prefixLetter" type="text" name="prefixLetter" />
                </AvGroup>
                <AvGroup>
                  <Label id="databaseVersionLabel" for="release-databaseVersion">
                    Database Version
                  </Label>
                  <AvField
                    id="release-databaseVersion"
                    type="text"
                    name="databaseVersion"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="wsVersionLabel" for="release-wsVersion">
                    Ws Version
                  </Label>
                  <AvField id="release-wsVersion" type="text" name="wsVersion" />
                </AvGroup>
                <AvGroup>
                  <Label id="tmaVersionLabel" for="release-tmaVersion">
                    Tma Version
                  </Label>
                  <AvField id="release-tmaVersion" type="text" name="tmaVersion" />
                </AvGroup>
                <AvGroup>
                  <Label id="portLabel" for="release-port">
                    Port
                  </Label>
                  <AvField id="release-port" type="string" className="form-control" name="port" />
                </AvGroup>
                <AvGroup>
                  <Label id="newFeaturesLabel" for="release-newFeatures">
                    New Features
                  </Label>
                  <AvInput id="release-newFeatures" type="textarea" name="newFeatures" />
                </AvGroup>
                <AvGroup>
                  <Label id="updatedFeaturesLabel" for="release-updatedFeatures">
                    Updated Features
                  </Label>
                  <AvInput id="release-updatedFeatures" type="textarea" name="updatedFeatures" />
                </AvGroup>
                <AvGroup>
                  <Label id="currentLabel" check>
                    <AvInput id="release-current" type="checkbox" className="form-control" name="current" />
                    Current
                  </Label>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/release" replace color="info">
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
  releaseEntity: storeState.release.entity,
  loading: storeState.release.loading,
  updating: storeState.release.updating,
  updateSuccess: storeState.release.updateSuccess
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
)(ReleaseUpdate);
