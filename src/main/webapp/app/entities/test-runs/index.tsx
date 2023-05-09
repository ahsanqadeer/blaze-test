import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRuns from './test-runs';
import TestRunsDetail from './test-runs-detail';
import TestRunsUpdate from './test-runs-update';
import TestRunsDeleteDialog from './test-runs-delete-dialog';

const TestRunsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRuns />} />
    <Route path="new" element={<TestRunsUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunsDetail />} />
      <Route path="edit" element={<TestRunsUpdate />} />
      <Route path="delete" element={<TestRunsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunsRoutes;
