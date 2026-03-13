package com.sangngo552004.musicapp.exception;

import com.sangngo552004.musicapp.dto.ApiResponse;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Override
    public Response toResponse(ForbiddenException exception) {
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(ApiResponse.of(Response.Status.FORBIDDEN.getStatusCode(), exception.getMessage(), null))
                .build();
    }
}
