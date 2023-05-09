import { ITestRuns } from 'app/shared/model/test-runs.model';
import { ITestCases } from 'app/shared/model/test-cases.model';
import { ITestStatuses } from 'app/shared/model/test-statuses.model';
import { ITestRunDetailAttachments } from 'app/shared/model/test-run-detail-attachments.model';
import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';

export interface ITestRunDetails {
  id?: number;
  resultDetail?: string | null;
  jiraId?: string | null;
  createdBy?: number | null;
  executedBy?: number | null;
  testRun?: ITestRuns | null;
  testCase?: ITestCases | null;
  status?: ITestStatuses | null;
  testrundetailattachmentsTestrundetails?: ITestRunDetailAttachments[] | null;
  testrunstepdetailsTestrundetails?: ITestRunStepDetails[] | null;
}

export const defaultValue: Readonly<ITestRunDetails> = {};
