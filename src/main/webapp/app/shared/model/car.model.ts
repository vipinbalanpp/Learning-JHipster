import { IOwner } from 'app/shared/model/owner.model';

export interface ICar {
  id?: number;
  name?: string;
  model?: string;
  price?: number;
  owner?: IOwner | null;
}

export const defaultValue: Readonly<ICar> = {};
