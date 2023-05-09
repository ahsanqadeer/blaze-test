import { ITemplateFields } from 'app/shared/model/template-fields.model';

export interface ITemplateFieldTypes {
  id?: number;
  type?: string;
  isList?: boolean;
  attachments?: boolean;
  templatefieldsTemplatefieldtypes?: ITemplateFields[] | null;
}

export const defaultValue: Readonly<ITemplateFieldTypes> = {
  isList: false,
  attachments: false,
};
