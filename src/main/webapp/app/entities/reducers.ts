import companies from 'app/entities/companies/companies.reducer';
import milestones from 'app/entities/milestones/milestones.reducer';
import permissions from 'app/entities/permissions/permissions.reducer';
import projects from 'app/entities/projects/projects.reducer';
import roles from 'app/entities/roles/roles.reducer';
import sections from 'app/entities/sections/sections.reducer';
import templateFieldTypes from 'app/entities/template-field-types/template-field-types.reducer';
import templateFields from 'app/entities/template-fields/template-fields.reducer';
import templates from 'app/entities/templates/templates.reducer';
import testCaseAttachments from 'app/entities/test-case-attachments/test-case-attachments.reducer';
import testCaseFieldAttachments from 'app/entities/test-case-field-attachments/test-case-field-attachments.reducer';
import testCaseFields from 'app/entities/test-case-fields/test-case-fields.reducer';
import testCasePriorities from 'app/entities/test-case-priorities/test-case-priorities.reducer';
import testCases from 'app/entities/test-cases/test-cases.reducer';
import testLevels from 'app/entities/test-levels/test-levels.reducer';
import testPlans from 'app/entities/test-plans/test-plans.reducer';
import testRunDetailAttachments from 'app/entities/test-run-detail-attachments/test-run-detail-attachments.reducer';
import testRunDetails from 'app/entities/test-run-details/test-run-details.reducer';
import testRunStepDetailAttachments from 'app/entities/test-run-step-detail-attachments/test-run-step-detail-attachments.reducer';
import testRunStepDetails from 'app/entities/test-run-step-details/test-run-step-details.reducer';
import testRuns from 'app/entities/test-runs/test-runs.reducer';
import testStatuses from 'app/entities/test-statuses/test-statuses.reducer';
import testSuites from 'app/entities/test-suites/test-suites.reducer';
import users from 'app/entities/users/users.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  companies,
  milestones,
  permissions,
  projects,
  roles,
  sections,
  templateFieldTypes,
  templateFields,
  templates,
  testCaseAttachments,
  testCaseFieldAttachments,
  testCaseFields,
  testCasePriorities,
  testCases,
  testLevels,
  testPlans,
  testRunDetailAttachments,
  testRunDetails,
  testRunStepDetailAttachments,
  testRunStepDetails,
  testRuns,
  testStatuses,
  testSuites,
  users,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
