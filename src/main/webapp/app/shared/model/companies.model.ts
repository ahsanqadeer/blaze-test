import dayjs from 'dayjs';
import { IProjects } from 'app/shared/model/projects.model';
import { ITemplateFields } from 'app/shared/model/template-fields.model';
import { ITemplates } from 'app/shared/model/templates.model';
import { IUsers } from 'app/shared/model/users.model';

export interface ICompanies {
  id?: number;
  country?: string | null;
  url?: string | null;
  name?: string | null;
  expectedNoOfUsers?: number | null;
  createdBy?: number;
  createdAt?: string;
  updatedBy?: number | null;
  updatedAt?: string | null;
  projectsCompanies?: IProjects[] | null;
  templatefieldsCompanies?: ITemplateFields[] | null;
  templatesCompanies?: ITemplates[] | null;
  usersCompanies?: IUsers[] | null;
}

export const defaultValue: Readonly<ICompanies> = {};
