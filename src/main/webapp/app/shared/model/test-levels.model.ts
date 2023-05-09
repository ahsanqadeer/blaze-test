import { ITestRuns } from 'app/shared/model/test-runs.model';
import { ITestCases } from 'app/shared/model/test-cases.model';

export interface ITestLevels {
  id?: number;
  name?: string | null;
  testrunsTestlevels?: ITestRuns[] | null;
  testCases?: ITestCases[] | null;
}

export const defaultValue: Readonly<ITestLevels> = {};
