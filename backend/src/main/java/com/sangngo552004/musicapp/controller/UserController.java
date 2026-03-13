package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.dto.ApiResponse;
import com.sangngo552004.musicapp.dto.UpdateProfileRequest;
import com.sangngo552004.musicapp.dto.UserResponse;
import com.sangngo552004.musicapp.security.AuthenticatedUserPrincipal;
import com.sangngo552004.musicapp.security.Secured;
import com.sangngo552004.musicapp.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserService userService;

    @GET
    @Path("/me")
    @Secured
    public Response getProfile(@Context SecurityContext securityContext) {
        Long userId = extractUserId(securityContext);
        UserResponse response = userService.getProfile(userId);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Profile fetched successfully", response)).build();
    }

    @PUT
    @Path("/me")
    @Secured
    public Response updateProfile(@Context SecurityContext securityContext, @Valid UpdateProfileRequest request) {
        Long userId = extractUserId(securityContext);
        UserResponse response = userService.updateProfile(userId, request);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Profile updated successfully", response)).build();
    }

    private Long extractUserId(SecurityContext securityContext) {
        AuthenticatedUserPrincipal principal = (AuthenticatedUserPrincipal) securityContext.getUserPrincipal();
        return principal.getUserId();
    }
}
