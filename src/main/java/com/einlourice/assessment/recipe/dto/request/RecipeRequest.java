package com.einlourice.assessment.recipe.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
public class RecipeRequest implements Request {
    @NotBlank
    @Size(min = 1, max = 250)
    protected String name;
    @NotNull
    protected Boolean isVegetarian;
    @NotNull
    @PositiveOrZero
    protected Integer numberOfServings;

    @NotEmpty
    protected List<@Valid IngredientRequest> ingredientList;
    @NotEmpty
    protected List<@Valid InstructionRequest> instructionList;
}
