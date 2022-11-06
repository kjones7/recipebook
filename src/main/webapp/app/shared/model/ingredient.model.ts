import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';

export interface IIngredient {
  id?: number;
  name?: string;
  recipeIngredients?: IRecipeIngredient[] | null;
}

export const defaultValue: Readonly<IIngredient> = {};
