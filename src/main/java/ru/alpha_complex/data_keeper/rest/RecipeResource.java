package ru.alpha_complex.data_keeper.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alpha_complex.data_keeper.model.RecipeDTO;
import ru.alpha_complex.data_keeper.model.RecipeWithIngredientsDTO;
import ru.alpha_complex.data_keeper.service.RecipeService;
import ru.alpha_complex.data_keeper.util.ReferencedException;
import ru.alpha_complex.data_keeper.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeResource {

    private final RecipeService recipeService;

    public RecipeResource(final RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(recipeService.get(id));
    }

    @GetMapping("/{id}/with-ingredients")
    public ResponseEntity<RecipeWithIngredientsDTO> getRecipeWithIngredients(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipeWithIngredients(id));
    }


    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createRecipe(@RequestBody @Valid final RecipeDTO recipeDTO) {
        final Integer createdId = recipeService.create(recipeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateRecipe(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final RecipeDTO recipeDTO) {
        recipeService.update(id, recipeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRecipe(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = recipeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
