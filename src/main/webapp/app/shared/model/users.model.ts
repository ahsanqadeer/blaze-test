import dayjs from 'dayjs';
import { ICompanies } from 'app/shared/model/companies.model';
import { IProjects } from 'app/shared/model/projects.model';
import { IRoles } from 'app/shared/model/roles.model';

export interface IUsers {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  password?: string | null;
  lastActive?: string | null;
  status?: string | null;
  createdBy?: number | null;
  createdAt?: string | null;
  updatedBy?: number | null;
  updatedAt?: string | null;
  email?: string | null;
  isDeleted?: boolean;
  emailVerified?: boolean;
  provider?: string;
  emailVerificationToken?: string | null;
  forgotPasswordToken?: string | null;
  company?: ICompanies | null;
  projects?: IProjects[] | null;
  roles?: IRoles[] | null;
}

export const defaultValue: Readonly<IUsers> = {
  isDeleted: false,
  emailVerified: false,
};
