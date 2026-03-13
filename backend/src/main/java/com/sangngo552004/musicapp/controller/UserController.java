package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.dto.request.UpdateProfileRequest;
import com.sangngo552004.musicapp.dto.response.ApiResponse;
import com.sangngo552004.musicapp.dto.response.CursorResult;
import com.sangngo552004.musicapp.dto.response.FavoriteToggleResponse;
import com.sangngo552004.musicapp.dto.response.SongResponse;
import com.sangngo552004.musicapp.dto.response.UserResponse;
import com.sangngo552004.musicapp.security.AuthenticatedUserPrincipal;
import com.sangngo552004.musicapp.security.Secured;
import com.sangngo552004.musicapp.service.FavoriteService;
import com.sangngo552004.musicapp.service.UserService;
import jakarta.ws.rs.DefaultValue;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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

    @Inject
    private FavoriteService favoriteService;

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

    @POST
    @Path("/favorites/{songId}")
    @Secured
    public Response toggleFavorite(@Context SecurityContext securityContext, @PathParam("songId") Long songId) {
        Long userId = extractUserId(securityContext);
        FavoriteToggleResponse response = favoriteService.toggleFavorite(userId, songId);
        String message = response.isFavorited() ? "Song added to favorites" : "Song removed from favorites";
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), message, response)).build();
    }

    @GET
    @Path("/favorites")
    @Secured
    public Response getFavorites(
            @Context SecurityContext securityContext,
            @QueryParam("cursor") Long cursor,
            @DefaultValue("20") @QueryParam("limit") Integer limit
    ) {
        Long userId = extractUserId(securityContext);
        CursorResult<SongResponse> response = favoriteService.getFavoriteSongs(userId, cursor, limit);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Favorite songs fetched successfully", response)).build();
    }

    private Long extractUserId(SecurityContext securityContext) {
        AuthenticatedUserPrincipal principal = (AuthenticatedUserPrincipal) securityContext.getUserPrincipal();
        return principal.getUserId();
    }
}
