package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.dto.ApiResponse;
import com.sangngo552004.musicapp.dto.UpdateProfileRequest;
import com.sangngo552004.musicapp.dto.UserResponse;
import com.sangngo552004.musicapp.security.AuthenticatedUserPrincipal;
import com.sangngo552004.musicapp.service.UserService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @Test
    void getProfileShouldReturnAuthenticatedUsersProfile() {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(7L);
        userResponse.setEmail("user@example.com");

        when(securityContext.getUserPrincipal()).thenReturn(new AuthenticatedUserPrincipal(7L, "user@example.com"));
        when(userService.getProfile(7L)).thenReturn(userResponse);

        Response response = userController.getProfile(securityContext);
        ApiResponse<?> body = (ApiResponse<?>) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals("Profile fetched successfully", body.getMessage());
        assertEquals(userResponse, body.getData());
    }

    @Test
    void updateProfileShouldReturnUpdatedProfile() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Updated Name");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(7L);
        userResponse.setFullName("Updated Name");

        when(securityContext.getUserPrincipal()).thenReturn(new AuthenticatedUserPrincipal(7L, "user@example.com"));
        when(userService.updateProfile(7L, request)).thenReturn(userResponse);

        Response response = userController.updateProfile(securityContext, request);
        ApiResponse<?> body = (ApiResponse<?>) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals("Profile updated successfully", body.getMessage());
        assertEquals(userResponse, body.getData());
        verify(userService).updateProfile(7L, request);
    }
}
