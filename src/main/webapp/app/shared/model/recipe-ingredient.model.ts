import { IRecipe } from 'app/shared/model/recipe.model';

export interface IRecipeIngredient {
  id?: number;
  amount?: number;
  units?: string;
  recipe?: IRecipe | null;
}

export const defaultValue: Readonly<IRecipeIngredient> = {};
