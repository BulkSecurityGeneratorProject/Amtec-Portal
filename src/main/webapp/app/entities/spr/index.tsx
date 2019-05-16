import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Spr from './spr';
import SprDetail from './spr-detail';
import SprUpdate from './spr-update';
import SprDeleteDialog from './spr-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SprUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SprUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SprDetail} />
      <ErrorBoundaryRoute path={match.url} component={Spr} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SprDeleteDialog} />
  </>
);

export default Routes;
