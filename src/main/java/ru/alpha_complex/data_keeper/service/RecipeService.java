package ru.alpha_complex.data_keeper.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alpha_complex.data_keeper.domain.Recipe;
import ru.alpha_complex.data_keeper.domain.RecipeIngredient;
import ru.alpha_complex.data_keeper.model.RecipeDTO;
import ru.alpha_complex.data_keeper.model.RecipeWithIngredientsDTO;
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

    // Новый метод для получения рецепта с ингредиентами через native SQL
    public RecipeWithIngredientsDTO getRecipeWithIngredients(final Integer id) {
        List<Object[]> results = recipeRepository.findRecipeWithIngredientsNative(id);

        if (results.isEmpty()) {
            throw new NotFoundException();  // Можно создать более подробное исключение
        }

        // Создаем объект DTO для рецепта с ингредиентами
        RecipeWithIngredientsDTO recipeWithIngredients = new RecipeWithIngredientsDTO();
        Object[] firstRow = results.get(0);
        recipeWithIngredients.setId(((Number) firstRow[0]).longValue());
        recipeWithIngredients.setName((String) firstRow[1]);
        recipeWithIngredients.setDescription((String) firstRow[2]);

        List<RecipeWithIngredientsDTO.IngredientDTO> ingredients = new ArrayList<>();
        for (Object[] row : results) {
            RecipeWithIngredientsDTO.IngredientDTO ingredientDTO = new RecipeWithIngredientsDTO.IngredientDTO();
            ingredientDTO.setName((String) row[4]);
            ingredientDTO.setDescription((String) row[5]);
            ingredientDTO.setQuantity((BigDecimal) row[6]);
            ingredientDTO.setUnit((String) row[7]);
            ingredients.add(ingredientDTO);
        }
        recipeWithIngredients.setIngredients(ingredients);

        return recipeWithIngredients;
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
