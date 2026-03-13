package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.request.UpdateProfileRequest;
import com.sangngo552004.musicapp.dto.response.UserResponse;
import com.sangngo552004.musicapp.entity.User;
import com.sangngo552004.musicapp.exception.ResourceNotFoundException;
import com.sangngo552004.musicapp.mapper.UserMapper;
import com.sangngo552004.musicapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getProfileShouldReturnMappedUser() {
        User user = new User();
        user.setId(10L);

        UserResponse expected = new UserResponse();
        expected.setId(10L);
        expected.setEmail("user@example.com");

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expected);

        UserResponse actual = userService.getProfile(10L);

        assertEquals(expected, actual);
    }

    @Test
    void getProfileShouldThrowResourceNotFoundExceptionWhenMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> userService.getProfile(99L));

        assertEquals("User was not found", exception.getMessage());
    }

    @Test
    void updateProfileShouldPersistUpdatedFields() {
        User user = new User();
        user.setId(10L);
        user.setFullName("Old Name");
        user.setPassword(BCrypt.hashpw("oldSecret123", BCrypt.gensalt()));

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("New Name");
        request.setPassword("newSecret123");

        UserResponse expected = new UserResponse();
        expected.setId(10L);
        expected.setFullName("New Name");

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doAnswer(invocation -> {
            User source = invocation.getArgument(0);
            UpdateProfileRequest sourceRequest = invocation.getArgument(1);
            String hashedPassword = invocation.getArgument(2);

            if (sourceRequest.getFullName() != null && !sourceRequest.getFullName().isBlank()) {
                source.setFullName(sourceRequest.getFullName().trim());
            }
            if (hashedPassword != null) {
                source.setPassword(hashedPassword);
            }
            return null;
        }).when(userMapper).applyProfileUpdate(any(User.class), any(UpdateProfileRequest.class), any());
        when(userMapper.toResponse(any(User.class))).thenReturn(expected);

        UserResponse actual = userService.updateProfile(10L, request);

        assertEquals(expected, actual);
        assertEquals("New Name", user.getFullName());
        assertNotEquals("newSecret123", user.getPassword());
        assertTrue(BCrypt.checkpw("newSecret123", user.getPassword()));
        verify(userRepository).save(user);
    }
}
