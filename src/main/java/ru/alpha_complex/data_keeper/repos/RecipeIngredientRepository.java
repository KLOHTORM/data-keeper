package ru.alpha_complex.data_keeper.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alpha_complex.data_keeper.domain.Ingredient;
import ru.alpha_complex.data_keeper.domain.Recipe;
import ru.alpha_complex.data_keeper.domain.RecipeIngredient;


public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {

    RecipeIngredient findFirstByRecipe(Recipe recipe);

    RecipeIngredient findFirstByIngredient(Ingredient ingredient);

}
