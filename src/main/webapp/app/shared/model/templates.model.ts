import dayjs from 'dayjs';
import { ICompanies } from 'app/shared/model/companies.model';
import { ITemplateFields } from 'app/shared/model/template-fields.model';
import { IProjects } from 'app/shared/model/projects.model';
import { ITestCases } from 'app/shared/model/test-cases.model';

export interface ITemplates {
  id?: number;
  templateName?: string | null;
  createdAt?: string | null;
  createdBy?: number | null;
  company?: ICompanies;
  templateFields?: ITemplateFields[] | null;
  projectsDefaulttemplates?: IProjects[] | null;
  testcasesTemplates?: ITestCases[] | null;
}

export const defaultValue: Readonly<ITemplates> = {};
