import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './recipe-ingredient.reducer';

export const RecipeIngredientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const recipeIngredientEntity = useAppSelector(state => state.recipeIngredient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recipeIngredientDetailsHeading">Recipe Ingredient</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{recipeIngredientEntity.id}</dd>
          <dt>
            <span id="amount">Amount</span>
          </dt>
          <dd>{recipeIngredientEntity.amount}</dd>
          <dt>
            <span id="units">Units</span>
          </dt>
          <dd>{recipeIngredientEntity.units}</dd>
          <dt>Recipe</dt>
          <dd>{recipeIngredientEntity.recipe ? recipeIngredientEntity.recipe.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/recipe-ingredient" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/recipe-ingredient/${recipeIngredientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecipeIngredientDetail;
