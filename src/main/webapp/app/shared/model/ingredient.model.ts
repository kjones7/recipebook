import { IRecipe } from 'app/shared/model/recipe.model';
import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';

export interface IIngredient {
  id?: number;
  name?: string;
  recipes?: IRecipe[] | null;
  recipeIngredients?: IRecipeIngredient[] | null;
}

export const defaultValue: Readonly<IIngredient> = {};
