import { ITestCaseFields } from 'app/shared/model/test-case-fields.model';

export interface ITestCaseFieldAttachments {
  id?: number;
  url?: string;
  testCaseField?: ITestCaseFields;
}

export const defaultValue: Readonly<ITestCaseFieldAttachments> = {};
