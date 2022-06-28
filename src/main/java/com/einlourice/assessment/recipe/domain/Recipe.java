package com.einlourice.assessment.recipe.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "RECIPE")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_VEGETARIAN")
    private Boolean isVegetarian;

    @Column(name = "NUMBER_OF_SERVINGS")
    private Integer numberOfServings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "recipe")
    private List<Instruction> instructionList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "recipe")
    private List<Ingredient> ingredientList;

    public Recipe() {
        this.instructionList = new ArrayList<>();
        this.ingredientList = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        this.ingredientList.add(ingredient);
    }

    public void addInstruction(Instruction instruction) {
        instruction.setRecipe(this);
        this.instructionList.add(instruction);
    }

    public void clear() {
        this.instructionList.clear();
        this.ingredientList.clear();
    }

}
