import { ITestRunDetails } from 'app/shared/model/test-run-details.model';
import { ITestCaseFields } from 'app/shared/model/test-case-fields.model';
import { ITestStatuses } from 'app/shared/model/test-statuses.model';
import { ITestRunStepDetailAttachments } from 'app/shared/model/test-run-step-detail-attachments.model';

export interface ITestRunStepDetails {
  id?: number;
  actualResult?: string | null;
  testRunDetail?: ITestRunDetails | null;
  stepDetail?: ITestCaseFields | null;
  status?: ITestStatuses | null;
  testrunstepdetailattachmentsTestrunstepdetails?: ITestRunStepDetailAttachments[] | null;
}

export const defaultValue: Readonly<ITestRunStepDetails> = {};
