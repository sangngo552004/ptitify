package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.dto.ApiResponse;
import com.sangngo552004.musicapp.dto.AuthResponse;
import com.sangngo552004.musicapp.dto.LoginRequest;
import com.sangngo552004.musicapp.dto.RegisterRequest;
import com.sangngo552004.musicapp.dto.UserResponse;
import com.sangngo552004.musicapp.service.AuthService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerShouldReturnCreatedApiResponse() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse authResponse = new AuthResponse("access-token", "refresh-token", new UserResponse());

        when(authService.register(request)).thenReturn(authResponse);

        Response response = authController.register(request);
        ApiResponse<?> body = (ApiResponse<?>) response.getEntity();

        assertEquals(201, response.getStatus());
        assertEquals(201, body.getStatus());
        assertEquals("User registered successfully", body.getMessage());
        assertEquals(authResponse, body.getData());
    }

    @Test
    void loginShouldReturnOkApiResponse() {
        LoginRequest request = new LoginRequest();
        AuthResponse authResponse = new AuthResponse("access-token", "refresh-token", new UserResponse());

        when(authService.login(request)).thenReturn(authResponse);

        Response response = authController.login(request);
        ApiResponse<?> body = (ApiResponse<?>) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals("Login successful", body.getMessage());
        assertEquals(authResponse, body.getData());
    }

    @Test
    void forgotPasswordShouldReturnGenericSuccessResponse() {
        com.sangngo552004.musicapp.dto.ForgotPasswordRequest request =
                new com.sangngo552004.musicapp.dto.ForgotPasswordRequest();
        request.setEmail("user@example.com");

        Response response = authController.forgotPassword(request);
        ApiResponse<?> body = (ApiResponse<?>) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals("If the email exists, a reset token has been sent", body.getMessage());
        assertNull(body.getData());
        verify(authService).forgotPassword(request);
    }

    @Test
    void logoutShouldReturnSuccessResponse() {
        com.sangngo552004.musicapp.dto.RefreshTokenRequest request =
                new com.sangngo552004.musicapp.dto.RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        Response response = authController.logout(request);
        ApiResponse<?> body = (ApiResponse<?>) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals("Logout successful", body.getMessage());
        assertNull(body.getData());
        verify(authService).logout(request);
    }
}
