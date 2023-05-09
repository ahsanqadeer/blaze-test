import { ITestCases } from 'app/shared/model/test-cases.model';

export interface ITestCaseAttachments {
  id?: number;
  url?: string;
  testCase?: ITestCases;
}

export const defaultValue: Readonly<ITestCaseAttachments> = {};
