import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Release from './release';
import Spr from './spr';
import OutOfOffice from './out-of-office';
import TeamEvent from './team-event';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/release`} component={Release} />
      <ErrorBoundaryRoute path={`${match.url}/spr`} component={Spr} />
      <ErrorBoundaryRoute path={`${match.url}/out-of-office`} component={OutOfOffice} />
      <ErrorBoundaryRoute path={`${match.url}/team-event`} component={TeamEvent} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
