package ru.alpha_complex.data_keeper.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alpha_complex.data_keeper.domain.Ingredient;
import ru.alpha_complex.data_keeper.domain.Recipe;
import ru.alpha_complex.data_keeper.domain.RecipeIngredient;
import ru.alpha_complex.data_keeper.model.RecipeIngredientDTO;
import ru.alpha_complex.data_keeper.repos.IngredientRepository;
import ru.alpha_complex.data_keeper.repos.RecipeIngredientRepository;
import ru.alpha_complex.data_keeper.repos.RecipeRepository;
import ru.alpha_complex.data_keeper.util.NotFoundException;


@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeIngredientService(final RecipeIngredientRepository recipeIngredientRepository,
            final RecipeRepository recipeRepository,
            final IngredientRepository ingredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<RecipeIngredientDTO> findAll() {
        final List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAll(Sort.by("id"));
        return recipeIngredients.stream()
                .map(recipeIngredient -> mapToDTO(recipeIngredient, new RecipeIngredientDTO()))
                .toList();
    }

    public RecipeIngredientDTO get(final Integer id) {
        return recipeIngredientRepository.findById(id)
                .map(recipeIngredient -> mapToDTO(recipeIngredient, new RecipeIngredientDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final RecipeIngredientDTO recipeIngredientDTO) {
        final RecipeIngredient recipeIngredient = new RecipeIngredient();
        mapToEntity(recipeIngredientDTO, recipeIngredient);
        return recipeIngredientRepository.save(recipeIngredient).getId();
    }

    public void update(final Integer id, final RecipeIngredientDTO recipeIngredientDTO) {
        final RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(recipeIngredientDTO, recipeIngredient);
        recipeIngredientRepository.save(recipeIngredient);
    }

    public void delete(final Integer id) {
        recipeIngredientRepository.deleteById(id);
    }

    private RecipeIngredientDTO mapToDTO(final RecipeIngredient recipeIngredient,
            final RecipeIngredientDTO recipeIngredientDTO) {
        recipeIngredientDTO.setId(recipeIngredient.getId());
        recipeIngredientDTO.setQuantity(recipeIngredient.getQuantity());
        recipeIngredientDTO.setUnit(recipeIngredient.getUnit());
        recipeIngredientDTO.setRecipe(recipeIngredient.getRecipe() == null ? null : recipeIngredient.getRecipe().getId());
        recipeIngredientDTO.setIngredient(recipeIngredient.getIngredient() == null ? null : recipeIngredient.getIngredient().getId());
        return recipeIngredientDTO;
    }

    private RecipeIngredient mapToEntity(final RecipeIngredientDTO recipeIngredientDTO,
            final RecipeIngredient recipeIngredient) {
        recipeIngredient.setQuantity(recipeIngredientDTO.getQuantity());
        recipeIngredient.setUnit(recipeIngredientDTO.getUnit());
        final Recipe recipe = recipeIngredientDTO.getRecipe() == null ? null : recipeRepository.findById(recipeIngredientDTO.getRecipe())
                .orElseThrow(() -> new NotFoundException("recipe not found"));
        recipeIngredient.setRecipe(recipe);
        final Ingredient ingredient = recipeIngredientDTO.getIngredient() == null ? null : ingredientRepository.findById(recipeIngredientDTO.getIngredient())
                .orElseThrow(() -> new NotFoundException("ingredient not found"));
        recipeIngredient.setIngredient(ingredient);
        return recipeIngredient;
    }

}
