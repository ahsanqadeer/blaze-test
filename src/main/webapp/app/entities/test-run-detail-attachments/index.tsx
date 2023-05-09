import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRunDetailAttachments from './test-run-detail-attachments';
import TestRunDetailAttachmentsDetail from './test-run-detail-attachments-detail';
import TestRunDetailAttachmentsUpdate from './test-run-detail-attachments-update';
import TestRunDetailAttachmentsDeleteDialog from './test-run-detail-attachments-delete-dialog';

const TestRunDetailAttachmentsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRunDetailAttachments />} />
    <Route path="new" element={<TestRunDetailAttachmentsUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunDetailAttachmentsDetail />} />
      <Route path="edit" element={<TestRunDetailAttachmentsUpdate />} />
      <Route path="delete" element={<TestRunDetailAttachmentsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunDetailAttachmentsRoutes;
