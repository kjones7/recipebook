import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';

export interface IRecipe {
  id?: number;
  title?: string;
  servings?: number | null;
  instructions?: string | null;
  notes?: string | null;
  recipeIngredients?: IRecipeIngredient[] | null;
}

export const defaultValue: Readonly<IRecipe> = {};
