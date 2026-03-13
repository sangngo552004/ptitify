package com.sangngo552004.musicapp.dto;

import com.sangngo552004.musicapp.dto.request.RegisterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void shouldContainViolationWhenEmailIsInvalid() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("tester");
        request.setPassword("secret123");
        request.setEmail("invalid-email");
        request.setFullName("Test User");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(violation -> "email must be valid".equals(violation.getMessage())));
    }
}
