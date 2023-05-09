import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Companies from './companies';
import CompaniesDetail from './companies-detail';
import CompaniesUpdate from './companies-update';
import CompaniesDeleteDialog from './companies-delete-dialog';

const CompaniesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Companies />} />
    <Route path="new" element={<CompaniesUpdate />} />
    <Route path=":id">
      <Route index element={<CompaniesDetail />} />
      <Route path="edit" element={<CompaniesUpdate />} />
      <Route path="delete" element={<CompaniesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompaniesRoutes;
