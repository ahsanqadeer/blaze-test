import { ITestCases } from 'app/shared/model/test-cases.model';

export interface ITestCasePriorities {
  id?: number;
  name?: string;
  testcasesPriorities?: ITestCases[] | null;
}

export const defaultValue: Readonly<ITestCasePriorities> = {};
