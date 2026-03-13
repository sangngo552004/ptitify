package com.sangngo552004.musicapp.exception;

import com.sangngo552004.musicapp.dto.ApiResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthExceptionMapper implements ExceptionMapper<AuthException> {

    @Override
    public Response toResponse(AuthException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(ApiResponse.of(Response.Status.UNAUTHORIZED.getStatusCode(), exception.getMessage(), null))
                .build();
    }
}
