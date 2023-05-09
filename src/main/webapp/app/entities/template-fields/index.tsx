import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TemplateFields from './template-fields';
import TemplateFieldsDetail from './template-fields-detail';
import TemplateFieldsUpdate from './template-fields-update';
import TemplateFieldsDeleteDialog from './template-fields-delete-dialog';

const TemplateFieldsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TemplateFields />} />
    <Route path="new" element={<TemplateFieldsUpdate />} />
    <Route path=":id">
      <Route index element={<TemplateFieldsDetail />} />
      <Route path="edit" element={<TemplateFieldsUpdate />} />
      <Route path="delete" element={<TemplateFieldsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TemplateFieldsRoutes;
