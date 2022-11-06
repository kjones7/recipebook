import { IIngredient } from 'app/shared/model/ingredient.model';
import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';

export interface IRecipe {
  id?: number;
  title?: string;
  servings?: number | null;
  instructions?: string | null;
  notes?: string | null;
  ingredients?: IIngredient[] | null;
  recipeIngredients?: IRecipeIngredient[] | null;
}

export const defaultValue: Readonly<IRecipe> = {};
