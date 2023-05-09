import dayjs from 'dayjs';
import { ITestLevels } from 'app/shared/model/test-levels.model';
import { IMilestones } from 'app/shared/model/milestones.model';
import { ITestRunDetails } from 'app/shared/model/test-run-details.model';

export interface ITestRuns {
  id?: number;
  name?: string | null;
  description?: string | null;
  createdAt?: string | null;
  createdBy?: number | null;
  testLevel?: ITestLevels | null;
  mileStone?: IMilestones | null;
  testrundetailsTestruns?: ITestRunDetails[] | null;
}

export const defaultValue: Readonly<ITestRuns> = {};
