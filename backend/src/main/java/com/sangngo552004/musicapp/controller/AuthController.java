package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.dto.ApiResponse;
import com.sangngo552004.musicapp.dto.AuthResponse;
import com.sangngo552004.musicapp.dto.ForgotPasswordRequest;
import com.sangngo552004.musicapp.dto.GoogleLoginRequest;
import com.sangngo552004.musicapp.dto.LoginRequest;
import com.sangngo552004.musicapp.dto.RefreshTokenRequest;
import com.sangngo552004.musicapp.dto.RegisterRequest;
import com.sangngo552004.musicapp.dto.ResetPasswordRequest;
import com.sangngo552004.musicapp.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private AuthService authService;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.of(Response.Status.CREATED.getStatusCode(), "User registered successfully", response))
                .build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        AuthResponse response = authService.login(request);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Login successful", response)).build();
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(@Valid RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Token refreshed successfully", response)).build();
    }

    @POST
    @Path("/google")
    public Response googleLogin(@Valid GoogleLoginRequest request) {
        AuthResponse response = authService.loginWithGoogle(request);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Google login successful", response)).build();
    }

    @POST
    @Path("/forgot-password")
    public Response forgotPassword(@Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return Response.ok(ApiResponse.of(
                Response.Status.OK.getStatusCode(),
                "If the email exists, a reset token has been sent",
                null
        )).build();
    }

    @POST
    @Path("/reset-password")
    public Response resetPassword(@Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Password reset successful", null)).build();
    }
}
