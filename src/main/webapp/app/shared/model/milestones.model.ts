import dayjs from 'dayjs';
import { IProjects } from 'app/shared/model/projects.model';
import { ITestCases } from 'app/shared/model/test-cases.model';
import { ITestRuns } from 'app/shared/model/test-runs.model';

export interface IMilestones {
  id?: number;
  name?: string | null;
  description?: string | null;
  reference?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  isCompleted?: boolean | null;
  parentMilestone?: IMilestones | null;
  project?: IProjects | null;
  milestonesParentmilestones?: IMilestones[] | null;
  testcasesMilestones?: ITestCases[] | null;
  testrunsMilestones?: ITestRuns[] | null;
}

export const defaultValue: Readonly<IMilestones> = {
  isCompleted: false,
};
