import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ingredient from './ingredient';
import Recipe from './recipe';
import RecipeIngredient from './recipe-ingredient';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="ingredient/*" element={<Ingredient />} />
        <Route path="recipe/*" element={<Recipe />} />
        <Route path="recipe-ingredient/*" element={<RecipeIngredient />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
