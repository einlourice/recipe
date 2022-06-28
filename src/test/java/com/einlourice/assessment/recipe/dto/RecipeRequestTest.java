package com.einlourice.assessment.recipe.dto;

import com.einlourice.assessment.recipe.dto.request.IngredientRequest;
import com.einlourice.assessment.recipe.dto.request.InstructionRequest;
import com.einlourice.assessment.recipe.dto.request.RecipeRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

import static com.einlourice.assessment.recipe.util.ConstraintViolationUtil.getConstraintViolationByPropertyName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecipeRequestTest {

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
    public void validate_RecipeRequest_OK() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName("test");
        recipeRequest.setIsVegetarian(true);
        recipeRequest.setNumberOfServings(5);
        recipeRequest.setIngredientList(new ArrayList<>() {{
            add(new IngredientRequest("1"));
        }});
        recipeRequest.setInstructionList(new ArrayList<>() {{
            add(new InstructionRequest("1"));
        }});

        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void validate_NameNotBlank_Present() {
        RecipeRequest recipeRequest = new RecipeRequest();
        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);
        ConstraintViolation<RecipeRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "name");


        assertEquals("{javax.validation.constraints.NotBlank.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_NameSize_Present() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setName(RandomStringUtils.random(251, true, true));
        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);
        ConstraintViolation<RecipeRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "name");


        assertEquals("{javax.validation.constraints.Size.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_IsVegetarianNotNull_Present() {
        RecipeRequest recipeRequest = new RecipeRequest();
        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);
        ConstraintViolation<RecipeRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "isVegetarian");

        assertEquals("{javax.validation.constraints.NotNull.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_NumberOfServingsNotNull_Present() {
        RecipeRequest recipeRequest = new RecipeRequest();
        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);
        ConstraintViolation<RecipeRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "numberOfServings");

        assertEquals("{javax.validation.constraints.NotNull.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_NumberOfServingsPositiveOrZero_Present() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setNumberOfServings(-1);
        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);
        ConstraintViolation<RecipeRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "numberOfServings");

        assertEquals("{javax.validation.constraints.PositiveOrZero.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_IngredientListNotEmpty_Present() {
        RecipeRequest recipeRequest = new RecipeRequest();
        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);
        ConstraintViolation<RecipeRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "ingredientList");

        assertEquals("{javax.validation.constraints.NotEmpty.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_InstructionListNotEmpty_Present() {
        RecipeRequest recipeRequest = new RecipeRequest();
        Set<ConstraintViolation<RecipeRequest>> violations = validator.validate(recipeRequest);
        ConstraintViolation<RecipeRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "instructionList");

        assertEquals("{javax.validation.constraints.NotEmpty.message}", constraintViolation.getMessageTemplate());
    }
}
