import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestSuites from './test-suites';
import TestSuitesDetail from './test-suites-detail';
import TestSuitesUpdate from './test-suites-update';
import TestSuitesDeleteDialog from './test-suites-delete-dialog';

const TestSuitesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestSuites />} />
    <Route path="new" element={<TestSuitesUpdate />} />
    <Route path=":id">
      <Route index element={<TestSuitesDetail />} />
      <Route path="edit" element={<TestSuitesUpdate />} />
      <Route path="delete" element={<TestSuitesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestSuitesRoutes;
