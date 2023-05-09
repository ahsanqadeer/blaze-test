import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Permissions from './permissions';
import PermissionsDetail from './permissions-detail';
import PermissionsUpdate from './permissions-update';
import PermissionsDeleteDialog from './permissions-delete-dialog';

const PermissionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Permissions />} />
    <Route path="new" element={<PermissionsUpdate />} />
    <Route path=":id">
      <Route index element={<PermissionsDetail />} />
      <Route path="edit" element={<PermissionsUpdate />} />
      <Route path="delete" element={<PermissionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PermissionsRoutes;
