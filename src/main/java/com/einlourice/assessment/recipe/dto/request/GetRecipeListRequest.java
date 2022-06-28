package com.einlourice.assessment.recipe.dto.request;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class GetRecipeListRequest implements Request {
    private Boolean isVegetarian;
    @PositiveOrZero
    private Integer numberOfServings;
    @Size(min = 1)
    private String instruction;
    @Size(min = 1)
    private List<String> ingredientList;

    @Pattern(regexp = "include|exclude")
    private String ingredientFilterType;
}
