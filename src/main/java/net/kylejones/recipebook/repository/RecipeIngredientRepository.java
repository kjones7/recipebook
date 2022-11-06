package net.kylejones.recipebook.repository;

import java.util.List;
import java.util.Optional;
import net.kylejones.recipebook.domain.RecipeIngredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RecipeIngredient entity.
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    default Optional<RecipeIngredient> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RecipeIngredient> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RecipeIngredient> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct recipeIngredient from RecipeIngredient recipeIngredient left join fetch recipeIngredient.recipe left join fetch recipeIngredient.ingredient",
        countQuery = "select count(distinct recipeIngredient) from RecipeIngredient recipeIngredient"
    )
    Page<RecipeIngredient> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct recipeIngredient from RecipeIngredient recipeIngredient left join fetch recipeIngredient.recipe left join fetch recipeIngredient.ingredient"
    )
    List<RecipeIngredient> findAllWithToOneRelationships();

    @Query(
        "select recipeIngredient from RecipeIngredient recipeIngredient left join fetch recipeIngredient.recipe left join fetch recipeIngredient.ingredient where recipeIngredient.id =:id"
    )
    Optional<RecipeIngredient> findOneWithToOneRelationships(@Param("id") Long id);
}
