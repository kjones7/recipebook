import { IIngredient } from 'app/shared/model/ingredient.model';

export interface IRecipe {
  id?: number;
  title?: string;
  ingredients?: IIngredient[] | null;
}

export const defaultValue: Readonly<IRecipe> = {};
