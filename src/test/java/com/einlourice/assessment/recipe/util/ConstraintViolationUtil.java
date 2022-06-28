package com.einlourice.assessment.recipe.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ConstraintViolationUtil {

    public static <T> ConstraintViolation<T> getConstraintViolationByPropertyName(Set<ConstraintViolation<T>> violations, String propertyName) {
        return violations.stream()
                .filter(cv -> cv.getPropertyPath().toString().equalsIgnoreCase(propertyName))
                .findAny()
                .orElseThrow();
    }
}
