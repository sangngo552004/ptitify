package com.sangngo552004.musicapp.mapper;

import com.sangngo552004.musicapp.dto.request.RegisterRequest;
import com.sangngo552004.musicapp.dto.request.UpdateProfileRequest;
import com.sangngo552004.musicapp.dto.response.UserResponse;
import com.sangngo552004.musicapp.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserMapper {

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setGoogleAccountId(user.getGoogleAccountId());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

    public User toEntity(RegisterRequest request, String hashedPassword) {
        User user = new User();
        user.setUsername(normalizeNullable(request.getUsername()));
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setFullName(request.getFullName().trim());
        user.setPassword(hashedPassword);
        return user;
    }

    public void applyProfileUpdate(User user, UpdateProfileRequest request, String hashedPassword) {
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }

        if (hashedPassword != null) {
            user.setPassword(hashedPassword);
        }
    }

    private String normalizeNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
