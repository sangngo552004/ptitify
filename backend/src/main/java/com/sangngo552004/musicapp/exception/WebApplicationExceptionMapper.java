package com.sangngo552004.musicapp.exception;

import com.sangngo552004.musicapp.dto.ApiResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response originalResponse = exception.getResponse();
        int status = originalResponse.getStatus();
        String message = exception.getMessage() == null || exception.getMessage().isBlank()
                ? Response.Status.fromStatusCode(status).getReasonPhrase()
                : exception.getMessage();

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(ApiResponse.of(status, message, null))
                .build();
    }
}
