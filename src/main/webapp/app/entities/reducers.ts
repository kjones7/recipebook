import ingredient from 'app/entities/ingredient/ingredient.reducer';
import recipe from 'app/entities/recipe/recipe.reducer';
import recipeIngredient from 'app/entities/recipe-ingredient/recipe-ingredient.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  ingredient,
  recipe,
  recipeIngredient,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
