import dayjs from 'dayjs';
import { ITemplates } from 'app/shared/model/templates.model';
import { ICompanies } from 'app/shared/model/companies.model';
import { IMilestones } from 'app/shared/model/milestones.model';
import { ITestPlans } from 'app/shared/model/test-plans.model';
import { ITestSuites } from 'app/shared/model/test-suites.model';
import { IUsers } from 'app/shared/model/users.model';

export interface IProjects {
  id?: number;
  projectName?: string;
  description?: string | null;
  isactive?: boolean | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  defaultTemplate?: ITemplates | null;
  company?: ICompanies | null;
  milestonesProjects?: IMilestones[] | null;
  testplansProjects?: ITestPlans[] | null;
  testsuitesProjects?: ITestSuites[] | null;
  users?: IUsers[] | null;
}

export const defaultValue: Readonly<IProjects> = {
  isactive: false,
};
