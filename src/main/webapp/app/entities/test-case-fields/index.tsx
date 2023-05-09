import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCaseFields from './test-case-fields';
import TestCaseFieldsDetail from './test-case-fields-detail';
import TestCaseFieldsUpdate from './test-case-fields-update';
import TestCaseFieldsDeleteDialog from './test-case-fields-delete-dialog';

const TestCaseFieldsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCaseFields />} />
    <Route path="new" element={<TestCaseFieldsUpdate />} />
    <Route path=":id">
      <Route index element={<TestCaseFieldsDetail />} />
      <Route path="edit" element={<TestCaseFieldsUpdate />} />
      <Route path="delete" element={<TestCaseFieldsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCaseFieldsRoutes;
