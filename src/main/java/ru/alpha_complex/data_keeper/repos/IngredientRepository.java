package ru.alpha_complex.data_keeper.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alpha_complex.data_keeper.domain.Ingredient;


public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
