package ru.alpha_complex.data_keeper.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RecipeDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String description;

}
