package com.einlourice.assessment.recipe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructionRequest {
    @NotBlank
    @Size(min = 1, max = 250)
    private String step;
}
