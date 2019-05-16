import React from 'react';

import {
  Badge,
  Card,
  CardHeader,
  CardFooter,
  Media,
  Pagination,
  PaginationItem,
  PaginationLink,
  Progress,
  Table,
  Container,
  Row
} from 'reactstrap';
import { connect } from 'react-redux';
import { getEntities } from 'app/entities/spr/spr.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { cleanUserList, getUserOpenCount, getUserPercentage, getUserReviewedCount } from 'app/utils/spr_calculations';

export interface ISprUserPercChartProp extends StateProps, DispatchProps {
}

export interface ISprUserPercChartState {
  seconds: number;
}

export class SprUserPercChart extends React.Component<ISprUserPercChartProp, ISprUserPercChartState> {
  private interval: number;

  state: ISprUserPercChartState = {
    seconds: 0
  };

  componentDidMount() {
    this.props.getEntities();
    this.props.getUsers();
    this.interval = window.setInterval(() => this.tick(), 180000);
  }

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  // We will refresh the components every 3 Minutes
  tick() {
    this.setState(prevState => ({
      seconds: prevState.seconds + 1
    }));
    this.props.getEntities();
    this.props.getUsers();
  }
  getPercentages = (user, entities) => {
    const percentages = getUserPercentage(user, entities);
    return (
      <>
        <span className="mr-2">{percentages.percentage}%</span>
        <div>
          <Progress
            max={percentages.total}
            value={percentages.totalUser}
            barClassName="bg-danger"
          />
        </div>
      </>
    );
  };

  render() {
    const { entities, users } = this.props;
    const userList = cleanUserList(users);
    return (
      <>
          {/* Table */}
          <Row>
            <div className="col">
              <Card className="shadow">
                <CardHeader className="border-0">
                  <h3 className="mb-0">SPR Overview</h3>
                </CardHeader>
                <Table className="align-items-center table-flush" >
                  <thead className="thead-light">
                  <tr>
                    <th scope="col">User</th>
                    <th scope="col">Total Open</th>
                    <th scope="col">Reviewed</th>
                    <th scope="col">% owned of total SPRs</th>
                    <th scope="col" />
                  </tr>
                  </thead>
                  <tbody>
                  {userList.map((user, key) => (
                    <tr key={key}>
                      <th scope="row">
                        {user.firstName !== undefined && user.lastName !== undefined
                          ? user.firstName + ' ' + user.lastName
                          : user.login}
                      </th>
                      <td>
                        {getUserOpenCount(user, entities)}
                      </td>
                      <td>
                        {getUserReviewedCount(user, entities)}
                      </td>
                      <td>
                        <div className="d-flex align-items-center">
                          {this.getPercentages(user, entities)}
                          {/*<span className="mr-2">60%</span>*/}
                          {/*<div>*/}
                            {/*<Progress*/}
                              {/*max="100"*/}
                              {/*value="60"*/}
                              {/*barClassName="bg-danger"*/}
                            {/*/>*/}
                          {/*</div>*/}
                        </div>
                      </td>
                    </tr>
                  ))}
                  </tbody>
                </Table>
              </Card>
            </div>
          </Row>
      </>
    );
  }
}

const mapStateToProps = ({ spr, userManagement }) => ({
  entities: spr.entities,
  users: userManagement.users
});

const mapDispatchToProps = {
  getEntities,
  getUsers
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SprUserPercChart);
