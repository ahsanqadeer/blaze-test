import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';

export interface ITestRunStepDetailAttachments {
  id?: number;
  url?: string;
  testRunStepDetail?: ITestRunStepDetails;
}

export const defaultValue: Readonly<ITestRunStepDetailAttachments> = {};
