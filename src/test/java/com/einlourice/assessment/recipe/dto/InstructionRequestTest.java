package com.einlourice.assessment.recipe.dto;

import com.einlourice.assessment.recipe.dto.request.InstructionRequest;
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

public class InstructionRequestTest {

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
        InstructionRequest instructionRequest = new InstructionRequest();
        instructionRequest.setStep("1");

        Set<ConstraintViolation<InstructionRequest>> violations = validator.validate(instructionRequest);

        assertTrue(violations.isEmpty());
    }


    @Test
    public void validate_StepNotBlank_Present() {
        InstructionRequest instructionRequest = new InstructionRequest();
        instructionRequest.setStep("");

        Set<ConstraintViolation<InstructionRequest>> violations = validator.validate(instructionRequest);
        ConstraintViolation<InstructionRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "step");

        assertEquals("{javax.validation.constraints.NotBlank.message}", constraintViolation.getMessageTemplate());
    }

    @Test
    public void validate_StepSize_Present() {
        InstructionRequest instructionRequest = new InstructionRequest();
        instructionRequest.setStep(RandomStringUtils.random(251, true, true));

        Set<ConstraintViolation<InstructionRequest>> violations = validator.validate(instructionRequest);
        ConstraintViolation<InstructionRequest> constraintViolation = getConstraintViolationByPropertyName(violations, "step");

        assertEquals("{javax.validation.constraints.Size.message}", constraintViolation.getMessageTemplate());
    }
}
