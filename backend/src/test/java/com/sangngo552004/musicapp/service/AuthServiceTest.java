package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.AuthResponse;
import com.sangngo552004.musicapp.dto.LoginRequest;
import com.sangngo552004.musicapp.dto.RefreshTokenRequest;
import com.sangngo552004.musicapp.dto.RegisterRequest;
import com.sangngo552004.musicapp.dto.UserResponse;
import com.sangngo552004.musicapp.entity.PasswordResetToken;
import com.sangngo552004.musicapp.entity.User;
import com.sangngo552004.musicapp.exception.AuthException;
import com.sangngo552004.musicapp.exception.ValidationException;
import com.sangngo552004.musicapp.repository.PasswordResetTokenRepository;
import com.sangngo552004.musicapp.repository.UserRepository;
import com.sangngo552004.musicapp.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private GoogleTokenVerifier googleTokenVerifier;

    @Mock
    private EmailService emailService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldPersistHashedPasswordAndReturnTokens() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("tester");
        request.setPassword("secret123");
        request.setEmail("USER@Example.com");
        request.setFullName("Test User");

        UserResponse mappedUser = buildUserResponse(1L, "tester", "user@example.com", "Test User");

        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("tester")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        when(jwtProvider.generateAccessToken(any(User.class))).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(any(User.class))).thenReturn("refresh-token");
        when(userMapper.toResponse(any(User.class))).thenReturn(mappedUser);

        AuthResponse response = authService.register(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(mappedUser, response.getUser());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User persisted = captor.getValue();
        assertEquals("tester", persisted.getUsername());
        assertEquals("user@example.com", persisted.getEmail());
        assertEquals("Test User", persisted.getFullName());
        assertNotEquals("secret123", persisted.getPassword());
        assertTrue(BCrypt.checkpw("secret123", persisted.getPassword()));
    }

    @Test
    void registerShouldThrowValidationExceptionWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("tester");
        request.setPassword("secret123");
        request.setEmail("user@example.com");
        request.setFullName("Test User");

        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.register(request));

        assertEquals("Email is already in use", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginShouldReturnTokensWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("tester");
        request.setPassword("secret123");

        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword(BCrypt.hashpw("secret123", BCrypt.gensalt()));

        UserResponse mappedUser = buildUserResponse(1L, "tester", "user@example.com", "Test User");

        when(userRepository.findByEmailOrUsername("tester")).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("refresh-token");
        when(userMapper.toResponse(user)).thenReturn(mappedUser);

        AuthResponse response = authService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(mappedUser, response.getUser());
    }

    @Test
    void loginShouldThrowAuthExceptionWhenPasswordIsWrong() {
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("tester");
        request.setPassword("wrong-password");

        User user = new User();
        user.setPassword(BCrypt.hashpw("secret123", BCrypt.gensalt()));

        when(userRepository.findByEmailOrUsername("tester")).thenReturn(Optional.of(user));

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void refreshTokenShouldLoadUserAndReturnNewTokens() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        Claims claims = Jwts.claims().subject("5").build();
        User user = new User();
        user.setId(5L);
        user.setEmail("user@example.com");

        UserResponse mappedUser = buildUserResponse(5L, "tester", "user@example.com", "Test User");

        when(jwtProvider.parseRefreshToken("refresh-token")).thenReturn(claims);
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn("new-access-token");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("new-refresh-token");
        when(userMapper.toResponse(user)).thenReturn(mappedUser);

        AuthResponse response = authService.refreshToken(request);

        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        assertEquals(mappedUser, response.getUser());
    }

    @Test
    void resetPasswordShouldThrowValidationExceptionWhenTokenExpired() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("expired-token");
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        token.setUser(new User());

        when(passwordResetTokenRepository.findActiveToken("expired-token")).thenReturn(Optional.of(token));

        com.sangngo552004.musicapp.dto.ResetPasswordRequest request =
                new com.sangngo552004.musicapp.dto.ResetPasswordRequest();
        request.setToken("expired-token");
        request.setNewPassword("newSecret123");

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.resetPassword(request));

        assertEquals("Reset token has expired", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    private UserResponse buildUserResponse(Long id, String username, String email, String fullName) {
        UserResponse response = new UserResponse();
        response.setId(id);
        response.setUsername(username);
        response.setEmail(email);
        response.setFullName(fullName);
        return response;
    }
}
