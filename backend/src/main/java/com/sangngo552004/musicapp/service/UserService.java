package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.request.UpdateProfileRequest;
import com.sangngo552004.musicapp.dto.response.UserResponse;
import com.sangngo552004.musicapp.entity.User;
import com.sangngo552004.musicapp.exception.ResourceNotFoundException;
import com.sangngo552004.musicapp.mapper.UserMapper;
import com.sangngo552004.musicapp.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    public UserResponse getProfile(Long userId) {
        User user = findUser(userId);
        return userMapper.toResponse(user);
    }

    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findUser(userId);
        String hashedPassword = request.getPassword() != null && !request.getPassword().isBlank()
                ? BCrypt.hashpw(request.getPassword(), BCrypt.gensalt())
                : null;
        userMapper.applyProfileUpdate(user, request, hashedPassword);

        return userMapper.toResponse(userRepository.save(user));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found"));
    }
}
