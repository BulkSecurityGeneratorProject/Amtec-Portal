import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OutOfOffice from './out-of-office';
import OutOfOfficeDetail from './out-of-office-detail';
import OutOfOfficeUpdate from './out-of-office-update';
import OutOfOfficeDeleteDialog from './out-of-office-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OutOfOfficeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OutOfOfficeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OutOfOfficeDetail} />
      <ErrorBoundaryRoute path={match.url} component={OutOfOffice} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OutOfOfficeDeleteDialog} />
  </>
);

export default Routes;
