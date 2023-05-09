import { ICompanies } from 'app/shared/model/companies.model';
import { ITemplateFieldTypes } from 'app/shared/model/template-field-types.model';
import { ITestCaseFields } from 'app/shared/model/test-case-fields.model';
import { ITemplates } from 'app/shared/model/templates.model';

export interface ITemplateFields {
  id?: number;
  fieldName?: string | null;
  company?: ICompanies;
  templateFieldType?: ITemplateFieldTypes;
  testcasefieldsTemplatefields?: ITestCaseFields[] | null;
  templates?: ITemplates[] | null;
}

export const defaultValue: Readonly<ITemplateFields> = {};
