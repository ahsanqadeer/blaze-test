import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Milestones from './milestones';
import MilestonesDetail from './milestones-detail';
import MilestonesUpdate from './milestones-update';
import MilestonesDeleteDialog from './milestones-delete-dialog';

const MilestonesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Milestones />} />
    <Route path="new" element={<MilestonesUpdate />} />
    <Route path=":id">
      <Route index element={<MilestonesDetail />} />
      <Route path="edit" element={<MilestonesUpdate />} />
      <Route path="delete" element={<MilestonesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MilestonesRoutes;
