import dayjs from 'dayjs';
import { ITestSuites } from 'app/shared/model/test-suites.model';
import { ITestCases } from 'app/shared/model/test-cases.model';

export interface ISections {
  id?: number;
  name?: string | null;
  description?: string | null;
  createdAt?: string | null;
  createdBy?: number | null;
  updatedAt?: string | null;
  updatedBy?: number | null;
  testSuite?: ITestSuites | null;
  parentSection?: ISections | null;
  sectionsParentsections?: ISections[] | null;
  testcasesSections?: ITestCases[] | null;
}

export const defaultValue: Readonly<ISections> = {};
