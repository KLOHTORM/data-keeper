package ru.alpha_complex.data_keeper.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RecipeIngredientDTO {

    private Integer id;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "45.08")
    private BigDecimal quantity;

    @NotNull
    @Size(max = 50)
    private String unit;

    @NotNull
    private Integer recipe;

    @NotNull
    private Integer ingredient;

}
