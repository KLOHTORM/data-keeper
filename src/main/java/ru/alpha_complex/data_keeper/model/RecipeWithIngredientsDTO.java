package ru.alpha_complex.data_keeper.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class RecipeWithIngredientsDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String description;

    @NotNull
    private List<IngredientDTO> ingredients;

    @Getter
    @Setter
    public static class IngredientDTO {

        @NotNull
        @Size(max = 255)
        private String name;

        @NotNull
        private String description;

        @NotNull
        private BigDecimal quantity;

        @NotNull
        @Size(max = 50)
        private String unit;
    }
}
