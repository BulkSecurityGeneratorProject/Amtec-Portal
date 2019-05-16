import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Release from './release';
import ReleaseDetail from './release-detail';
import ReleaseUpdate from './release-update';
import ReleaseDeleteDialog from './release-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReleaseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReleaseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReleaseDetail} />
      <ErrorBoundaryRoute path={match.url} component={Release} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ReleaseDeleteDialog} />
  </>
);

export default Routes;
