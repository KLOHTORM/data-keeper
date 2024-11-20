package ru.alpha_complex.data_keeper.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alpha_complex.data_keeper.domain.Ingredient;
import ru.alpha_complex.data_keeper.domain.RecipeIngredient;
import ru.alpha_complex.data_keeper.model.IngredientDTO;
import ru.alpha_complex.data_keeper.repos.IngredientRepository;
import ru.alpha_complex.data_keeper.repos.RecipeIngredientRepository;
import ru.alpha_complex.data_keeper.util.NotFoundException;
import ru.alpha_complex.data_keeper.util.ReferencedWarning;


@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public IngredientService(final IngredientRepository ingredientRepository,
            final RecipeIngredientRepository recipeIngredientRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public List<IngredientDTO> findAll() {
        final List<Ingredient> ingredients = ingredientRepository.findAll(Sort.by("id"));
        return ingredients.stream()
                .map(ingredient -> mapToDTO(ingredient, new IngredientDTO()))
                .toList();
    }

    public IngredientDTO get(final Integer id) {
        return ingredientRepository.findById(id)
                .map(ingredient -> mapToDTO(ingredient, new IngredientDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final IngredientDTO ingredientDTO) {
        final Ingredient ingredient = new Ingredient();
        mapToEntity(ingredientDTO, ingredient);
        return ingredientRepository.save(ingredient).getId();
    }

    public void update(final Integer id, final IngredientDTO ingredientDTO) {
        final Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(ingredientDTO, ingredient);
        ingredientRepository.save(ingredient);
    }

    public void delete(final Integer id) {
        ingredientRepository.deleteById(id);
    }

    private IngredientDTO mapToDTO(final Ingredient ingredient, final IngredientDTO ingredientDTO) {
        ingredientDTO.setId(ingredient.getId());
        ingredientDTO.setName(ingredient.getName());
        ingredientDTO.setDescription(ingredient.getDescription());
        return ingredientDTO;
    }

    private Ingredient mapToEntity(final IngredientDTO ingredientDTO, final Ingredient ingredient) {
        ingredient.setName(ingredientDTO.getName());
        ingredient.setDescription(ingredientDTO.getDescription());
        return ingredient;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final RecipeIngredient ingredientRecipeIngredient = recipeIngredientRepository.findFirstByIngredient(ingredient);
        if (ingredientRecipeIngredient != null) {
            referencedWarning.setKey("ingredient.recipeIngredient.ingredient.referenced");
            referencedWarning.addParam(ingredientRecipeIngredient.getId());
            return referencedWarning;
        }
        return null;
    }

}
