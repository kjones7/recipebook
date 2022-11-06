import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RecipeIngredient from './recipe-ingredient';
import RecipeIngredientDetail from './recipe-ingredient-detail';
import RecipeIngredientUpdate from './recipe-ingredient-update';
import RecipeIngredientDeleteDialog from './recipe-ingredient-delete-dialog';

const RecipeIngredientRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RecipeIngredient />} />
    <Route path="new" element={<RecipeIngredientUpdate />} />
    <Route path=":id">
      <Route index element={<RecipeIngredientDetail />} />
      <Route path="edit" element={<RecipeIngredientUpdate />} />
      <Route path="delete" element={<RecipeIngredientDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RecipeIngredientRoutes;
