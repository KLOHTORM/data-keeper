package ru.alpha_complex.data_keeper.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import ru.alpha_complex.data_keeper.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    // Нативный SQL-запрос с JOIN для получения рецепта и его ингредиентов
    @Query(value = "SELECT r.id as recipe_id, " +
            "r.name as recipe_name, " +
            "r.description as recipe_description, " +
            "i.id as ingredient_id, " +
            "i.name as ingredient_name, " +
            "i.description as ingredient_description, " +
            "ri.quantity, " +
            "ri.unit " +
            "FROM recipes r " +
            "JOIN recipe_ingredients ri ON r.id = ri.recipe_id " +
            "JOIN ingredients i ON ri.ingredient_id = i.id " +
            "WHERE r.id = :recipeId",
            nativeQuery = true)
    List<Object[]> findRecipeWithIngredientsNative(@Param("recipeId") Integer recipeId);
}
