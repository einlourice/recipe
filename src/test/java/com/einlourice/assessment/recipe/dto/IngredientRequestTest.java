package com.einlourice.assessment.recipe.dto;

import com.einlourice.assessment.recipe.dto.request.IngredientRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.einlourice.assessment.recipe.util.ConstraintViolationUtil.getConstraintViolationByPropertyName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IngredientRequestTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void validate_ValidInputs_OK() {
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setName("1");

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(ingredientRequest);

        assertTrue(violations.isEmpty());
    }


    @Test
    public void validate_NameNotBlank_Present() {
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setName(" ");

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(ingredientRequest);
        ConstraintViolation<IngredientRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "name");

        assertEquals("{javax.validation.constraints.NotBlank.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_NameSize_Present() {
        IngredientRequest ingredientRequest = new IngredientRequest();
        ingredientRequest.setName(RandomStringUtils.random(251, true, true));

        Set<ConstraintViolation<IngredientRequest>> violations = validator.validate(ingredientRequest);
        ConstraintViolation<IngredientRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "name");

        assertEquals("{javax.validation.constraints.Size.message}", constraintViolation.getMessageTemplate());
    }

}
