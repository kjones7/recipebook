import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('RecipeIngredient e2e test', () => {
  const recipeIngredientPageUrl = '/recipe-ingredient';
  const recipeIngredientPageUrlPattern = new RegExp('/recipe-ingredient(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const recipeIngredientSample = { amount: 77974, units: 'generating' };

  let recipeIngredient;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/recipe-ingredients+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/recipe-ingredients').as('postEntityRequest');
    cy.intercept('DELETE', '/api/recipe-ingredients/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (recipeIngredient) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recipe-ingredients/${recipeIngredient.id}`,
      }).then(() => {
        recipeIngredient = undefined;
      });
    }
  });

  it('RecipeIngredients menu should load RecipeIngredients page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('recipe-ingredient');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RecipeIngredient').should('exist');
    cy.url().should('match', recipeIngredientPageUrlPattern);
  });

  describe('RecipeIngredient page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(recipeIngredientPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RecipeIngredient page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/recipe-ingredient/new$'));
        cy.getEntityCreateUpdateHeading('RecipeIngredient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', recipeIngredientPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/recipe-ingredients',
          body: recipeIngredientSample,
        }).then(({ body }) => {
          recipeIngredient = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/recipe-ingredients+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/recipe-ingredients?page=0&size=20>; rel="last",<http://localhost/api/recipe-ingredients?page=0&size=20>; rel="first"',
              },
              body: [recipeIngredient],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(recipeIngredientPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RecipeIngredient page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('recipeIngredient');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', recipeIngredientPageUrlPattern);
      });

      it('edit button click should load edit RecipeIngredient page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RecipeIngredient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', recipeIngredientPageUrlPattern);
      });

      it('edit button click should load edit RecipeIngredient page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RecipeIngredient');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', recipeIngredientPageUrlPattern);
      });

      it('last delete button click should delete instance of RecipeIngredient', () => {
        cy.intercept('GET', '/api/recipe-ingredients/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('recipeIngredient').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', recipeIngredientPageUrlPattern);

        recipeIngredient = undefined;
      });
    });
  });

  describe('new RecipeIngredient page', () => {
    beforeEach(() => {
      cy.visit(`${recipeIngredientPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RecipeIngredient');
    });

    it('should create an instance of RecipeIngredient', () => {
      cy.get(`[data-cy="amount"]`).type('737').should('have.value', '737');

      cy.get(`[data-cy="units"]`).type('ROI').should('have.value', 'ROI');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        recipeIngredient = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', recipeIngredientPageUrlPattern);
    });
  });
});
