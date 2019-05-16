import 'react-toastify/dist/ReactToastify.css';
// import './app.scss';
import '../static/assets/vendor/nucleo/css/nucleo.css';
import '../static/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css';
import '../static/assets/scss/argon-dashboard-react.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Card } from 'reactstrap';
import { BrowserRouter as Router } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import { hot } from 'react-hot-loader';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import Header from 'app/shared/layout/header/header';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';

const baseHref = document
  .querySelector('base')
  .getAttribute('href')
  .replace(/\/$/, '');

export interface IAppProps extends StateProps, DispatchProps {}

export class App extends React.Component<IAppProps> {
  componentDidMount() {
    this.props.getSession();
    this.props.getProfile();
  }

  getStyle = () => {
    if (!this.props.menuHidden) return '100px';
    else return '0px';
  };

  getBackgroundColor = () => {
    if (this.props.menuHidden) return '#32325d';
    else return '#fafafa';
  };

  render() {
    const paddingTop = this.getStyle();
    const backgroundColor = this.getBackgroundColor();
    return (
      <Router basename={baseHref}>
        <div className="app-container" style={{ paddingTop, backgroundColor }}>
          <ToastContainer
            position={toast.POSITION.TOP_LEFT}
            className="toastify-container"
            toastClassName="toastify-toast"
          />
          {this.props.menuHidden ? '' :
            <ErrorBoundary>
              <Header
                isAuthenticated={this.props.isAuthenticated}
                isAdmin={this.props.isAdmin}
                ribbonEnv={this.props.ribbonEnv}
                isInProduction={this.props.isInProduction}
                isSwaggerEnabled={this.props.isSwaggerEnabled}
              />
            </ErrorBoundary>
          }
          <div className="container-fluid view-container" id="app-view-container">
            <Card className="jh-card">
              <ErrorBoundary>
                <AppRoutes />
              </ErrorBoundary>
            </Card>
          </div>
        </div>
      </Router>
    );
  }
}

const mapStateToProps = ({ authentication, applicationProfile }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN]),
  ribbonEnv: applicationProfile.ribbonEnv,
  isInProduction: applicationProfile.inProduction,
  isSwaggerEnabled: applicationProfile.isSwaggerEnabled,
  menuHidden: applicationProfile.menuHidden
});

const mapDispatchToProps = { getSession, getProfile };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(hot(module)(App));
