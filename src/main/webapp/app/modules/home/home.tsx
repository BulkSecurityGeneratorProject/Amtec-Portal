import './home.scss';

import React from 'react';

import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { getSession } from 'app/shared/reducers/authentication';
import { AuthenticatedView, UnAuthenticatedView } from 'app/modules/home/home_components';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {

  componentDidMount() {
    this.props.getSession();
  }

  render() {
    const { account, isAuthenticated } = this.props;
    return (
      <Row>
        {isAuthenticated && <AuthenticatedView />}
        {!isAuthenticated && <UnAuthenticatedView />}
      </Row>
    );
  }
}

const mapStateToProps = ({ authentication }) => ({
  account: authentication.account,
  isAuthenticated: authentication.isAuthenticated
});

const mapDispatchToProps = {
  getSession
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
