package ru.alpha_complex.data_keeper.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Ingredients")
@Getter
@Setter
public class Ingredient {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(
            nullable = false,
            name = "\"description\"",
            columnDefinition = "longtext"
    )
    private String description;

    @OneToMany(mappedBy = "ingredient")
    private Set<RecipeIngredient> ingredientRecipeIngredients;

}
