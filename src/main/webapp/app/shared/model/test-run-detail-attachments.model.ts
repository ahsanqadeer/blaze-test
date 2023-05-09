import { ITestRunDetails } from 'app/shared/model/test-run-details.model';

export interface ITestRunDetailAttachments {
  id?: number;
  url?: string;
  testRunDetail?: ITestRunDetails;
}

export const defaultValue: Readonly<ITestRunDetailAttachments> = {};
