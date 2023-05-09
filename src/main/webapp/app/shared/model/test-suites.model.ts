import dayjs from 'dayjs';
import { IProjects } from 'app/shared/model/projects.model';
import { ISections } from 'app/shared/model/sections.model';
import { ITestCases } from 'app/shared/model/test-cases.model';

export interface ITestSuites {
  id?: number;
  testSuiteName?: string | null;
  description?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  project?: IProjects | null;
  sectionsTestsuites?: ISections[] | null;
  testcasesTestsuites?: ITestCases[] | null;
}

export const defaultValue: Readonly<ITestSuites> = {};
