import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestLevels from './test-levels';
import TestLevelsDetail from './test-levels-detail';
import TestLevelsUpdate from './test-levels-update';
import TestLevelsDeleteDialog from './test-levels-delete-dialog';

const TestLevelsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestLevels />} />
    <Route path="new" element={<TestLevelsUpdate />} />
    <Route path=":id">
      <Route index element={<TestLevelsDetail />} />
      <Route path="edit" element={<TestLevelsUpdate />} />
      <Route path="delete" element={<TestLevelsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestLevelsRoutes;
