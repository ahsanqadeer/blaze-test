import { ITestRunDetails } from 'app/shared/model/test-run-details.model';
import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';

export interface ITestStatuses {
  id?: number;
  statusName?: string | null;
  testrundetailsStatuses?: ITestRunDetails[] | null;
  testrunstepdetailsStatuses?: ITestRunStepDetails[] | null;
}

export const defaultValue: Readonly<ITestStatuses> = {};
