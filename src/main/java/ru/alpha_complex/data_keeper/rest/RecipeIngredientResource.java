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
import ru.alpha_complex.data_keeper.model.RecipeIngredientDTO;
import ru.alpha_complex.data_keeper.service.RecipeIngredientService;


@RestController
@RequestMapping(value = "/api/recipeIngredients", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeIngredientResource {

    private final RecipeIngredientService recipeIngredientService;

    public RecipeIngredientResource(final RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeIngredientDTO>> getAllRecipeIngredients() {
        return ResponseEntity.ok(recipeIngredientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeIngredientDTO> getRecipeIngredient(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(recipeIngredientService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createRecipeIngredient(
            @RequestBody @Valid final RecipeIngredientDTO recipeIngredientDTO) {
        final Integer createdId = recipeIngredientService.create(recipeIngredientDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateRecipeIngredient(
            @PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final RecipeIngredientDTO recipeIngredientDTO) {
        recipeIngredientService.update(id, recipeIngredientDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRecipeIngredient(
            @PathVariable(name = "id") final Integer id) {
        recipeIngredientService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
