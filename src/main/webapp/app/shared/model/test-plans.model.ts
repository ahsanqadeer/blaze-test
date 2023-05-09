import dayjs from 'dayjs';
import { IProjects } from 'app/shared/model/projects.model';

export interface ITestPlans {
  id?: number;
  name?: string | null;
  description?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  project?: IProjects | null;
}

export const defaultValue: Readonly<ITestPlans> = {};
