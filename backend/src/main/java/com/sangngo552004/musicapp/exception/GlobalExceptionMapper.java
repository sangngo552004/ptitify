package com.sangngo552004.musicapp.exception;

import com.sangngo552004.musicapp.dto.ApiResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(ApiResponse.of(
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "An unexpected error occurred",
                        null
                ))
                .build();
    }
}
