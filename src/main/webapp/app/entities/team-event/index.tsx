import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TeamEvent from './team-event';
import TeamEventDetail from './team-event-detail';
import TeamEventUpdate from './team-event-update';
import TeamEventDeleteDialog from './team-event-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TeamEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TeamEventUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TeamEventDetail} />
      <ErrorBoundaryRoute path={match.url} component={TeamEvent} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TeamEventDeleteDialog} />
  </>
);

export default Routes;
