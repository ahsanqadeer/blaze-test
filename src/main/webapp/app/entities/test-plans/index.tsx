import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestPlans from './test-plans';
import TestPlansDetail from './test-plans-detail';
import TestPlansUpdate from './test-plans-update';
import TestPlansDeleteDialog from './test-plans-delete-dialog';

const TestPlansRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestPlans />} />
    <Route path="new" element={<TestPlansUpdate />} />
    <Route path=":id">
      <Route index element={<TestPlansDetail />} />
      <Route path="edit" element={<TestPlansUpdate />} />
      <Route path="delete" element={<TestPlansDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestPlansRoutes;
