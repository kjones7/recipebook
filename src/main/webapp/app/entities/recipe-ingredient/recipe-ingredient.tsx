import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
import { getEntities } from './recipe-ingredient.reducer';

export const RecipeIngredient = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const recipeIngredientList = useAppSelector(state => state.recipeIngredient.entities);
  const loading = useAppSelector(state => state.recipeIngredient.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="recipe-ingredient-heading" data-cy="RecipeIngredientHeading">
        Recipe Ingredients
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/recipe-ingredient/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Recipe Ingredient
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {recipeIngredientList && recipeIngredientList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Amount</th>
                <th>Units</th>
                <th>Recipe</th>
                <th>Ingredient</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {recipeIngredientList.map((recipeIngredient, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/recipe-ingredient/${recipeIngredient.id}`} color="link" size="sm">
                      {recipeIngredient.id}
                    </Button>
                  </td>
                  <td>{recipeIngredient.amount}</td>
                  <td>{recipeIngredient.units}</td>
                  <td>
                    {recipeIngredient.recipe ? (
                      <Link to={`/recipe/${recipeIngredient.recipe.id}`}>{recipeIngredient.recipe.title}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {recipeIngredient.ingredient ? (
                      <Link to={`/ingredient/${recipeIngredient.ingredient.id}`}>{recipeIngredient.ingredient.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/recipe-ingredient/${recipeIngredient.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/recipe-ingredient/${recipeIngredient.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/recipe-ingredient/${recipeIngredient.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Recipe Ingredients found</div>
        )}
      </div>
    </div>
  );
};

export default RecipeIngredient;
