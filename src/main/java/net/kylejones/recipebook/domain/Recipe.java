package net.kylejones.recipebook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "title", length = 40, nullable = false)
    private String title;

    @DecimalMin(value = "0")
    @Column(name = "servings", precision = 21, scale = 2)
    private BigDecimal servings;

    @Size(max = 2000)
    @Column(name = "instructions", length = 2000)
    private String instructions;

    @Size(max = 2000)
    @Column(name = "notes", length = 2000)
    private String notes;

    @ManyToMany
    @JoinTable(
        name = "rel_recipe__ingredient",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @JsonIgnoreProperties(value = { "recipes", "recipeIngredients" }, allowSetters = true)
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe")
    @JsonIgnoreProperties(value = { "recipe", "ingredient" }, allowSetters = true)
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Recipe title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getServings() {
        return this.servings;
    }

    public Recipe servings(BigDecimal servings) {
        this.setServings(servings);
        return this;
    }

    public void setServings(BigDecimal servings) {
        this.servings = servings;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public Recipe instructions(String instructions) {
        this.setInstructions(instructions);
        return this;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getNotes() {
        return this.notes;
    }

    public Recipe notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Recipe ingredients(Set<Ingredient> ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public Recipe addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.getRecipes().add(this);
        return this;
    }

    public Recipe removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
        ingredient.getRecipes().remove(this);
        return this;
    }

    public Set<RecipeIngredient> getRecipeIngredients() {
        return this.recipeIngredients;
    }

    public void setRecipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        if (this.recipeIngredients != null) {
            this.recipeIngredients.forEach(i -> i.setRecipe(null));
        }
        if (recipeIngredients != null) {
            recipeIngredients.forEach(i -> i.setRecipe(this));
        }
        this.recipeIngredients = recipeIngredients;
    }

    public Recipe recipeIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.setRecipeIngredients(recipeIngredients);
        return this;
    }

    public Recipe addRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.add(recipeIngredient);
        recipeIngredient.setRecipe(this);
        return this;
    }

    public Recipe removeRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.remove(recipeIngredient);
        recipeIngredient.setRecipe(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return id != null && id.equals(((Recipe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", servings=" + getServings() +
            ", instructions='" + getInstructions() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
