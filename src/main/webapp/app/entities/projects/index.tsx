import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Projects from './projects';
import ProjectsDetail from './projects-detail';
import ProjectsUpdate from './projects-update';
import ProjectsDeleteDialog from './projects-delete-dialog';

const ProjectsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Projects />} />
    <Route path="new" element={<ProjectsUpdate />} />
    <Route path=":id">
      <Route index element={<ProjectsDetail />} />
      <Route path="edit" element={<ProjectsUpdate />} />
      <Route path="delete" element={<ProjectsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProjectsRoutes;
