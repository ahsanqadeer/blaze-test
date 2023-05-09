import { ITemplateFields } from 'app/shared/model/template-fields.model';
import { ITestCases } from 'app/shared/model/test-cases.model';
import { ITestCaseFieldAttachments } from 'app/shared/model/test-case-field-attachments.model';
import { ITestRunStepDetails } from 'app/shared/model/test-run-step-details.model';

export interface ITestCaseFields {
  id?: number;
  expectedResult?: string | null;
  value?: string | null;
  templateField?: ITemplateFields;
  testCase?: ITestCases;
  testcasefieldattachmentsTestcasefields?: ITestCaseFieldAttachments[] | null;
  testrunstepdetailsStepdetails?: ITestRunStepDetails[] | null;
}

export const defaultValue: Readonly<ITestCaseFields> = {};
