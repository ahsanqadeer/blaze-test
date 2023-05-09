import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCaseAttachments from './test-case-attachments';
import TestCaseAttachmentsDetail from './test-case-attachments-detail';
import TestCaseAttachmentsUpdate from './test-case-attachments-update';
import TestCaseAttachmentsDeleteDialog from './test-case-attachments-delete-dialog';

const TestCaseAttachmentsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCaseAttachments />} />
    <Route path="new" element={<TestCaseAttachmentsUpdate />} />
    <Route path=":id">
      <Route index element={<TestCaseAttachmentsDetail />} />
      <Route path="edit" element={<TestCaseAttachmentsUpdate />} />
      <Route path="delete" element={<TestCaseAttachmentsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCaseAttachmentsRoutes;
