package ru.alpha_complex.data_keeper.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alpha_complex.data_keeper.model.IngredientDTO;
import ru.alpha_complex.data_keeper.service.IngredientService;
import ru.alpha_complex.data_keeper.util.ReferencedException;
import ru.alpha_complex.data_keeper.util.ReferencedWarning;

@RestController
@RequestMapping(value = "/api/ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngredientResource {

    private final IngredientService ingredientService;

    public IngredientResource(final IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredient(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(ingredientService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createIngredient(
            @RequestBody @Valid final IngredientDTO ingredientDTO) {
        final Integer createdId = ingredientService.create(ingredientDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateIngredient(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final IngredientDTO ingredientDTO) {
        ingredientService.update(id, ingredientDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteIngredient(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = ingredientService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
