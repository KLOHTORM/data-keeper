package ru.alpha_complex.data_keeper.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alpha_complex.data_keeper.domain.Recipe;
import ru.alpha_complex.data_keeper.domain.RecipeIngredient;
import ru.alpha_complex.data_keeper.model.RecipeDTO;
import ru.alpha_complex.data_keeper.repos.RecipeIngredientRepository;
import ru.alpha_complex.data_keeper.repos.RecipeRepository;
import ru.alpha_complex.data_keeper.util.NotFoundException;
import ru.alpha_complex.data_keeper.util.ReferencedWarning;


@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeService(final RecipeRepository recipeRepository,
            final RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public List<RecipeDTO> findAll() {
        final List<Recipe> recipes = recipeRepository.findAll(Sort.by("id"));
        return recipes.stream()
                .map(recipe -> mapToDTO(recipe, new RecipeDTO()))
                .toList();
    }

    public RecipeDTO get(final Integer id) {
        return recipeRepository.findById(id)
                .map(recipe -> mapToDTO(recipe, new RecipeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final RecipeDTO recipeDTO) {
        final Recipe recipe = new Recipe();
        mapToEntity(recipeDTO, recipe);
        return recipeRepository.save(recipe).getId();
    }

    public void update(final Integer id, final RecipeDTO recipeDTO) {
        final Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(recipeDTO, recipe);
        recipeRepository.save(recipe);
    }

    public void delete(final Integer id) {
        recipeRepository.deleteById(id);
    }

    private RecipeDTO mapToDTO(final Recipe recipe, final RecipeDTO recipeDTO) {
        recipeDTO.setId(recipe.getId());
        recipeDTO.setName(recipe.getName());
        recipeDTO.setDescription(recipe.getDescription());
        return recipeDTO;
    }

    private Recipe mapToEntity(final RecipeDTO recipeDTO, final Recipe recipe) {
        recipe.setName(recipeDTO.getName());
        recipe.setDescription(recipeDTO.getDescription());
        return recipe;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final RecipeIngredient recipeRecipeIngredient = recipeIngredientRepository.findFirstByRecipe(recipe);
        if (recipeRecipeIngredient != null) {
            referencedWarning.setKey("recipe.recipeIngredient.recipe.referenced");
            referencedWarning.addParam(recipeRecipeIngredient.getId());
            return referencedWarning;
        }
        return null;
    }

}
