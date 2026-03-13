package com.sangngo552004.musicapp.exception;

import com.sangngo552004.musicapp.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> violations = exception.getConstraintViolations()
                .stream()
                .map(this::formatViolation)
                .collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(ApiResponse.of(Response.Status.BAD_REQUEST.getStatusCode(), "Validation failed", violations))
                .build();
    }

    private String formatViolation(ConstraintViolation<?> violation) {
        return violation.getPropertyPath() + ": " + violation.getMessage();
    }
}
