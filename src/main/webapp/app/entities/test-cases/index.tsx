import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCases from './test-cases';
import TestCasesDetail from './test-cases-detail';
import TestCasesUpdate from './test-cases-update';
import TestCasesDeleteDialog from './test-cases-delete-dialog';

const TestCasesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCases />} />
    <Route path="new" element={<TestCasesUpdate />} />
    <Route path=":id">
      <Route index element={<TestCasesDetail />} />
      <Route path="edit" element={<TestCasesUpdate />} />
      <Route path="delete" element={<TestCasesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCasesRoutes;
