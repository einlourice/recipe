package com.einlourice.assessment.recipe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse implements Response {
    private String detailMessage;
    private Integer httpStatusCode;
}
