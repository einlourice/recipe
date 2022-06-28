package com.einlourice.assessment.recipe.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RecipeResponse implements Response {

    private Long id;
    private String name;
    private Boolean isVegetarian;
    private Integer numberOfServings;
    private List<InstructionResponse> instructionList;
    private List<IngredientResponse> ingredientList;

}
