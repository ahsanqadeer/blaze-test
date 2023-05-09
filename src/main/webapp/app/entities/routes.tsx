import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Companies from './companies';
import Milestones from './milestones';
import Permissions from './permissions';
import Projects from './projects';
import Roles from './roles';
import Sections from './sections';
import TemplateFieldTypes from './template-field-types';
import TemplateFields from './template-fields';
import Templates from './templates';
import TestCaseAttachments from './test-case-attachments';
import TestCaseFieldAttachments from './test-case-field-attachments';
import TestCaseFields from './test-case-fields';
import TestCasePriorities from './test-case-priorities';
import TestCases from './test-cases';
import TestLevels from './test-levels';
import TestPlans from './test-plans';
import TestRunDetailAttachments from './test-run-detail-attachments';
import TestRunDetails from './test-run-details';
import TestRunStepDetailAttachments from './test-run-step-detail-attachments';
import TestRunStepDetails from './test-run-step-details';
import TestRuns from './test-runs';
import TestStatuses from './test-statuses';
import TestSuites from './test-suites';
import Users from './users';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="companies/*" element={<Companies />} />
        <Route path="milestones/*" element={<Milestones />} />
        <Route path="permissions/*" element={<Permissions />} />
        <Route path="projects/*" element={<Projects />} />
        <Route path="roles/*" element={<Roles />} />
        <Route path="sections/*" element={<Sections />} />
        <Route path="template-field-types/*" element={<TemplateFieldTypes />} />
        <Route path="template-fields/*" element={<TemplateFields />} />
        <Route path="templates/*" element={<Templates />} />
        <Route path="test-case-attachments/*" element={<TestCaseAttachments />} />
        <Route path="test-case-field-attachments/*" element={<TestCaseFieldAttachments />} />
        <Route path="test-case-fields/*" element={<TestCaseFields />} />
        <Route path="test-case-priorities/*" element={<TestCasePriorities />} />
        <Route path="test-cases/*" element={<TestCases />} />
        <Route path="test-levels/*" element={<TestLevels />} />
        <Route path="test-plans/*" element={<TestPlans />} />
        <Route path="test-run-detail-attachments/*" element={<TestRunDetailAttachments />} />
        <Route path="test-run-details/*" element={<TestRunDetails />} />
        <Route path="test-run-step-detail-attachments/*" element={<TestRunStepDetailAttachments />} />
        <Route path="test-run-step-details/*" element={<TestRunStepDetails />} />
        <Route path="test-runs/*" element={<TestRuns />} />
        <Route path="test-statuses/*" element={<TestStatuses />} />
        <Route path="test-suites/*" element={<TestSuites />} />
        <Route path="users/*" element={<Users />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
