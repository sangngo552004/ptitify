package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.UserResponse;
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
}
