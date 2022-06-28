package com.einlourice.assessment.recipe.query;

import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchCriteria {
    private Boolean isVegetarian;
    private Integer numberOfServings;
    private String instruction;
    private List<String> ingredientList;
    private String ingredientFilterType;
}
