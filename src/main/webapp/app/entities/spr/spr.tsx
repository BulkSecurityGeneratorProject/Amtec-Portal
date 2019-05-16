import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { byteSize, ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './spr.reducer';
import { ISpr } from 'app/shared/model/spr.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';

export interface ISprProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ISprState {
  search: string;
}

export class Spr extends React.Component<ISprProps, ISprState> {
  state: ISprState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
    this.props.getUsers();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.setState({ search: '' }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { sprList, match, users } = this.props;
    return (
      <div>
        <h2 id="spr-heading">
          Sprs
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Spr
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Territory</th>
                <th>Number</th>
                <th>Full Number</th>
                <th>Description</th>
                <th>Priority</th>
                <th>Resolution</th>
                <th>Jira Link</th>
                <th>Reviewer Id</th>
                <th>Owner</th>
                <th>Release</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sprList.map((spr, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${spr.id}`} color="link" size="sm">
                      {spr.id}
                    </Button>
                  </td>
                  <td>{spr.territory}</td>
                  <td>{spr.number}</td>
                  <td>{spr.fullNumber}</td>
                  <td>{spr.description}</td>
                  <td>{spr.priority}</td>
                  <td>{spr.resolution}</td>
                  <td>{spr.jiraLink}</td>
                  <td>
                    {users.map((user, key) => {
                      if (user.id === spr.reviewerId) {
                        if (user.firstName !== undefined && user.lastName !== undefined) {
                          return user.firstName + ' ' + user.lastName;
                        } else {
                          return user.login;
                        }
                      }
                    })}
                  </td>
                  <td>{spr.user ? spr.user.login : ''}</td>
                  <td>{spr.release ? <Link to={`release/${spr.release.id}`}>{spr.release.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${spr.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${spr.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${spr.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ spr, userManagement }: IRootState) => ({
  sprList: spr.entities,
  users: userManagement.users
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
  getUsers
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Spr);
