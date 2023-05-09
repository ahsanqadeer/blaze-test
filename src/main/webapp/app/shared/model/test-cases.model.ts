import dayjs from 'dayjs';
import { ITestSuites } from 'app/shared/model/test-suites.model';
import { ISections } from 'app/shared/model/sections.model';
import { ITestCasePriorities } from 'app/shared/model/test-case-priorities.model';
import { ITemplates } from 'app/shared/model/templates.model';
import { IMilestones } from 'app/shared/model/milestones.model';
import { ITestLevels } from 'app/shared/model/test-levels.model';
import { ITestCaseAttachments } from 'app/shared/model/test-case-attachments.model';
import { ITestCaseFields } from 'app/shared/model/test-case-fields.model';
import { ITestRunDetails } from 'app/shared/model/test-run-details.model';

export interface ITestCases {
  id?: number;
  title?: string | null;
  estimate?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  precondition?: string | null;
  description?: string | null;
  isAutomated?: boolean | null;
  testSuite?: ITestSuites | null;
  section?: ISections | null;
  priority?: ITestCasePriorities;
  template?: ITemplates | null;
  milestone?: IMilestones | null;
  testLevels?: ITestLevels[] | null;
  testcaseattachmentsTestcases?: ITestCaseAttachments[] | null;
  testcasefieldsTestcases?: ITestCaseFields[] | null;
  testrundetailsTestcases?: ITestRunDetails[] | null;
}

export const defaultValue: Readonly<ITestCases> = {
  isAutomated: false,
};
