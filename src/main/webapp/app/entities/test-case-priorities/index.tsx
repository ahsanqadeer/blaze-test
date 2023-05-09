import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCasePriorities from './test-case-priorities';
import TestCasePrioritiesDetail from './test-case-priorities-detail';
import TestCasePrioritiesUpdate from './test-case-priorities-update';
import TestCasePrioritiesDeleteDialog from './test-case-priorities-delete-dialog';

const TestCasePrioritiesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCasePriorities />} />
    <Route path="new" element={<TestCasePrioritiesUpdate />} />
    <Route path=":id">
      <Route index element={<TestCasePrioritiesDetail />} />
      <Route path="edit" element={<TestCasePrioritiesUpdate />} />
      <Route path="delete" element={<TestCasePrioritiesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCasePrioritiesRoutes;
