import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TemplateFieldTypes from './template-field-types';
import TemplateFieldTypesDetail from './template-field-types-detail';
import TemplateFieldTypesUpdate from './template-field-types-update';
import TemplateFieldTypesDeleteDialog from './template-field-types-delete-dialog';

const TemplateFieldTypesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TemplateFieldTypes />} />
    <Route path="new" element={<TemplateFieldTypesUpdate />} />
    <Route path=":id">
      <Route index element={<TemplateFieldTypesDetail />} />
      <Route path="edit" element={<TemplateFieldTypesUpdate />} />
      <Route path="delete" element={<TemplateFieldTypesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TemplateFieldTypesRoutes;
