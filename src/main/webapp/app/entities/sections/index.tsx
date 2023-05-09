import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Sections from './sections';
import SectionsDetail from './sections-detail';
import SectionsUpdate from './sections-update';
import SectionsDeleteDialog from './sections-delete-dialog';

const SectionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Sections />} />
    <Route path="new" element={<SectionsUpdate />} />
    <Route path=":id">
      <Route index element={<SectionsDetail />} />
      <Route path="edit" element={<SectionsUpdate />} />
      <Route path="delete" element={<SectionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SectionsRoutes;
