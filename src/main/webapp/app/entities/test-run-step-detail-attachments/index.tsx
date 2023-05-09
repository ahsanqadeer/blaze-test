import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestRunStepDetailAttachments from './test-run-step-detail-attachments';
import TestRunStepDetailAttachmentsDetail from './test-run-step-detail-attachments-detail';
import TestRunStepDetailAttachmentsUpdate from './test-run-step-detail-attachments-update';
import TestRunStepDetailAttachmentsDeleteDialog from './test-run-step-detail-attachments-delete-dialog';

const TestRunStepDetailAttachmentsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestRunStepDetailAttachments />} />
    <Route path="new" element={<TestRunStepDetailAttachmentsUpdate />} />
    <Route path=":id">
      <Route index element={<TestRunStepDetailAttachmentsDetail />} />
      <Route path="edit" element={<TestRunStepDetailAttachmentsUpdate />} />
      <Route path="delete" element={<TestRunStepDetailAttachmentsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestRunStepDetailAttachmentsRoutes;
