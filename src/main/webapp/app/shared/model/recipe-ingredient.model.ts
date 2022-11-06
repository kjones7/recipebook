import { IRecipe } from 'app/shared/model/recipe.model';
import { IIngredient } from 'app/shared/model/ingredient.model';

export interface IRecipeIngredient {
  id?: number;
  amount?: number;
  units?: string;
  recipe?: IRecipe | null;
  ingredient?: IIngredient | null;
}

export const defaultValue: Readonly<IRecipeIngredient> = {};
