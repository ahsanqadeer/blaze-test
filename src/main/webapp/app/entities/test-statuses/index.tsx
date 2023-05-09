import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestStatuses from './test-statuses';
import TestStatusesDetail from './test-statuses-detail';
import TestStatusesUpdate from './test-statuses-update';
import TestStatusesDeleteDialog from './test-statuses-delete-dialog';

const TestStatusesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestStatuses />} />
    <Route path="new" element={<TestStatusesUpdate />} />
    <Route path=":id">
      <Route index element={<TestStatusesDetail />} />
      <Route path="edit" element={<TestStatusesUpdate />} />
      <Route path="delete" element={<TestStatusesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestStatusesRoutes;
