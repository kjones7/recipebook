import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRecipe } from 'app/shared/model/recipe.model';
import { getEntities as getRecipes } from 'app/entities/recipe/recipe.reducer';
import { IIngredient } from 'app/shared/model/ingredient.model';
import { getEntities as getIngredients } from 'app/entities/ingredient/ingredient.reducer';
import { IRecipeIngredient } from 'app/shared/model/recipe-ingredient.model';
import { getEntity, updateEntity, createEntity, reset } from './recipe-ingredient.reducer';

export const RecipeIngredientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const recipes = useAppSelector(state => state.recipe.entities);
  const ingredients = useAppSelector(state => state.ingredient.entities);
  const recipeIngredientEntity = useAppSelector(state => state.recipeIngredient.entity);
  const loading = useAppSelector(state => state.recipeIngredient.loading);
  const updating = useAppSelector(state => state.recipeIngredient.updating);
  const updateSuccess = useAppSelector(state => state.recipeIngredient.updateSuccess);

  const handleClose = () => {
    navigate('/recipe-ingredient');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRecipes({}));
    dispatch(getIngredients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...recipeIngredientEntity,
      ...values,
      recipe: recipes.find(it => it.id.toString() === values.recipe.toString()),
      ingredient: ingredients.find(it => it.id.toString() === values.ingredient.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...recipeIngredientEntity,
          recipe: recipeIngredientEntity?.recipe?.id,
          ingredient: recipeIngredientEntity?.ingredient?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="recipebookApp.recipeIngredient.home.createOrEditLabel" data-cy="RecipeIngredientCreateUpdateHeading">
            Create or edit a Recipe Ingredient
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="recipe-ingredient-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Amount"
                id="recipe-ingredient-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  min: { value: 0, message: 'This field should be at least 0.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Units"
                id="recipe-ingredient-units"
                name="units"
                data-cy="units"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 20, message: 'This field cannot be longer than 20 characters.' },
                }}
              />
              <ValidatedField id="recipe-ingredient-recipe" name="recipe" data-cy="recipe" label="Recipe" type="select">
                <option value="" key="0" />
                {recipes
                  ? recipes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="recipe-ingredient-ingredient" name="ingredient" data-cy="ingredient" label="Ingredient" type="select">
                <option value="" key="0" />
                {ingredients
                  ? ingredients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/recipe-ingredient" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RecipeIngredientUpdate;
