import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TestCaseFieldAttachments from './test-case-field-attachments';
import TestCaseFieldAttachmentsDetail from './test-case-field-attachments-detail';
import TestCaseFieldAttachmentsUpdate from './test-case-field-attachments-update';
import TestCaseFieldAttachmentsDeleteDialog from './test-case-field-attachments-delete-dialog';

const TestCaseFieldAttachmentsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TestCaseFieldAttachments />} />
    <Route path="new" element={<TestCaseFieldAttachmentsUpdate />} />
    <Route path=":id">
      <Route index element={<TestCaseFieldAttachmentsDetail />} />
      <Route path="edit" element={<TestCaseFieldAttachmentsUpdate />} />
      <Route path="delete" element={<TestCaseFieldAttachmentsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestCaseFieldAttachmentsRoutes;
