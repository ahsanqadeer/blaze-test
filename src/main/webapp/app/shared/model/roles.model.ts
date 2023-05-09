import { IPermissions } from 'app/shared/model/permissions.model';
import { IUsers } from 'app/shared/model/users.model';

export interface IRoles {
  id?: number;
  roleName?: string;
  isdefault?: boolean | null;
  permissions?: IPermissions[] | null;
  users?: IUsers[] | null;
}

export const defaultValue: Readonly<IRoles> = {
  isdefault: false,
};
