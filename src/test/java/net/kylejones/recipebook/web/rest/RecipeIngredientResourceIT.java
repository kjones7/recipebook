package net.kylejones.recipebook.web.rest;

import static net.kylejones.recipebook.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import net.kylejones.recipebook.IntegrationTest;
import net.kylejones.recipebook.domain.RecipeIngredient;
import net.kylejones.recipebook.repository.RecipeIngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecipeIngredientResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecipeIngredientResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final String DEFAULT_UNITS = "AAAAAAAAAA";
    private static final String UPDATED_UNITS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recipe-ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeIngredientMockMvc;

    private RecipeIngredient recipeIngredient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecipeIngredient createEntity(EntityManager em) {
        RecipeIngredient recipeIngredient = new RecipeIngredient().amount(DEFAULT_AMOUNT).units(DEFAULT_UNITS);
        return recipeIngredient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecipeIngredient createUpdatedEntity(EntityManager em) {
        RecipeIngredient recipeIngredient = new RecipeIngredient().amount(UPDATED_AMOUNT).units(UPDATED_UNITS);
        return recipeIngredient;
    }

    @BeforeEach
    public void initTest() {
        recipeIngredient = createEntity(em);
    }

    @Test
    @Transactional
    void createRecipeIngredient() throws Exception {
        int databaseSizeBeforeCreate = recipeIngredientRepository.findAll().size();
        // Create the RecipeIngredient
        restRecipeIngredientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isCreated());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeCreate + 1);
        RecipeIngredient testRecipeIngredient = recipeIngredientList.get(recipeIngredientList.size() - 1);
        assertThat(testRecipeIngredient.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testRecipeIngredient.getUnits()).isEqualTo(DEFAULT_UNITS);
    }

    @Test
    @Transactional
    void createRecipeIngredientWithExistingId() throws Exception {
        // Create the RecipeIngredient with an existing ID
        recipeIngredient.setId(1L);

        int databaseSizeBeforeCreate = recipeIngredientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeIngredientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeIngredientRepository.findAll().size();
        // set the field null
        recipeIngredient.setAmount(null);

        // Create the RecipeIngredient, which fails.

        restRecipeIngredientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isBadRequest());

        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = recipeIngredientRepository.findAll().size();
        // set the field null
        recipeIngredient.setUnits(null);

        // Create the RecipeIngredient, which fails.

        restRecipeIngredientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isBadRequest());

        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecipeIngredients() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get all the recipeIngredientList
        restRecipeIngredientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipeIngredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].units").value(hasItem(DEFAULT_UNITS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecipeIngredientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(recipeIngredientRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecipeIngredientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recipeIngredientRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecipeIngredientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recipeIngredientRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecipeIngredientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(recipeIngredientRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRecipeIngredient() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        // Get the recipeIngredient
        restRecipeIngredientMockMvc
            .perform(get(ENTITY_API_URL_ID, recipeIngredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipeIngredient.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.units").value(DEFAULT_UNITS));
    }

    @Test
    @Transactional
    void getNonExistingRecipeIngredient() throws Exception {
        // Get the recipeIngredient
        restRecipeIngredientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipeIngredient() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();

        // Update the recipeIngredient
        RecipeIngredient updatedRecipeIngredient = recipeIngredientRepository.findById(recipeIngredient.getId()).get();
        // Disconnect from session so that the updates on updatedRecipeIngredient are not directly saved in db
        em.detach(updatedRecipeIngredient);
        updatedRecipeIngredient.amount(UPDATED_AMOUNT).units(UPDATED_UNITS);

        restRecipeIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecipeIngredient.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecipeIngredient))
            )
            .andExpect(status().isOk());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
        RecipeIngredient testRecipeIngredient = recipeIngredientList.get(recipeIngredientList.size() - 1);
        assertThat(testRecipeIngredient.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testRecipeIngredient.getUnits()).isEqualTo(UPDATED_UNITS);
    }

    @Test
    @Transactional
    void putNonExistingRecipeIngredient() throws Exception {
        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();
        recipeIngredient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeIngredient.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipeIngredient() throws Exception {
        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();
        recipeIngredient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipeIngredient() throws Exception {
        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();
        recipeIngredient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeIngredientMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipeIngredientWithPatch() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();

        // Update the recipeIngredient using partial update
        RecipeIngredient partialUpdatedRecipeIngredient = new RecipeIngredient();
        partialUpdatedRecipeIngredient.setId(recipeIngredient.getId());

        partialUpdatedRecipeIngredient.amount(UPDATED_AMOUNT);

        restRecipeIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeIngredient.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipeIngredient))
            )
            .andExpect(status().isOk());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
        RecipeIngredient testRecipeIngredient = recipeIngredientList.get(recipeIngredientList.size() - 1);
        assertThat(testRecipeIngredient.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testRecipeIngredient.getUnits()).isEqualTo(DEFAULT_UNITS);
    }

    @Test
    @Transactional
    void fullUpdateRecipeIngredientWithPatch() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();

        // Update the recipeIngredient using partial update
        RecipeIngredient partialUpdatedRecipeIngredient = new RecipeIngredient();
        partialUpdatedRecipeIngredient.setId(recipeIngredient.getId());

        partialUpdatedRecipeIngredient.amount(UPDATED_AMOUNT).units(UPDATED_UNITS);

        restRecipeIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipeIngredient.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecipeIngredient))
            )
            .andExpect(status().isOk());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
        RecipeIngredient testRecipeIngredient = recipeIngredientList.get(recipeIngredientList.size() - 1);
        assertThat(testRecipeIngredient.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testRecipeIngredient.getUnits()).isEqualTo(UPDATED_UNITS);
    }

    @Test
    @Transactional
    void patchNonExistingRecipeIngredient() throws Exception {
        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();
        recipeIngredient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipeIngredient.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipeIngredient() throws Exception {
        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();
        recipeIngredient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipeIngredient() throws Exception {
        int databaseSizeBeforeUpdate = recipeIngredientRepository.findAll().size();
        recipeIngredient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recipeIngredient))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecipeIngredient in the database
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipeIngredient() throws Exception {
        // Initialize the database
        recipeIngredientRepository.saveAndFlush(recipeIngredient);

        int databaseSizeBeforeDelete = recipeIngredientRepository.findAll().size();

        // Delete the recipeIngredient
        restRecipeIngredientMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipeIngredient.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findAll();
        assertThat(recipeIngredientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
