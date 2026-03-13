package com.sangngo552004.musicapp.service;

import com.sangngo552004.musicapp.dto.AuthResponse;
import com.sangngo552004.musicapp.dto.ForgotPasswordRequest;
import com.sangngo552004.musicapp.dto.GoogleLoginRequest;
import com.sangngo552004.musicapp.dto.LoginRequest;
import com.sangngo552004.musicapp.dto.RefreshTokenRequest;
import com.sangngo552004.musicapp.dto.RegisterRequest;
import com.sangngo552004.musicapp.dto.ResetPasswordRequest;
import com.sangngo552004.musicapp.entity.PasswordResetToken;
import com.sangngo552004.musicapp.entity.User;
import com.sangngo552004.musicapp.exception.AuthException;
import com.sangngo552004.musicapp.exception.ValidationException;
import com.sangngo552004.musicapp.repository.PasswordResetTokenRepository;
import com.sangngo552004.musicapp.repository.UserRepository;
import com.sangngo552004.musicapp.security.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Stateless
public class AuthService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Inject
    private JwtProvider jwtProvider;

    @Inject
    private GoogleTokenVerifier googleTokenVerifier;

    @Inject
    private EmailService emailService;

    @Inject
    private UserMapper userMapper;

    public AuthResponse register(RegisterRequest request) {
        validateRegistration(request);

        User user = new User();
        user.setUsername(normalizeNullable(request.getUsername()));
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setFullName(request.getFullName().trim());
        user.setPassword(hashPassword(request.getPassword()));

        userRepository.save(user);
        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailOrUsername(request.getEmailOrUsername().trim())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (user.getPassword() == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid credentials");
        }

        return buildAuthResponse(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Claims claims = jwtProvider.parseRefreshToken(request.getRefreshToken());
        Long userId = Long.parseLong(claims.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("User was not found"));
        return buildAuthResponse(user);
    }

    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        GoogleTokenVerifier.GoogleUserInfo googleUser = googleTokenVerifier.verify(request.getGoogleIdToken());
        User user = userRepository.findByGoogleAccountId(googleUser.googleAccountId())
                .orElseGet(() -> findOrCreateGoogleUser(googleUser));
        return buildAuthResponse(user);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail().trim())
                .ifPresent(user -> {
                    passwordResetTokenRepository.markAllAsUsedForUser(user);

                    PasswordResetToken resetToken = new PasswordResetToken();
                    resetToken.setUser(user);
                    resetToken.setToken(generateResetToken());
                    resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
                    resetToken.setUsed(false);
                    passwordResetTokenRepository.save(resetToken);

                    emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());
                });
    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findActiveToken(request.getToken().trim())
                .orElseThrow(() -> new ValidationException("Reset token is invalid"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(hashPassword(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    private void validateRegistration(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ValidationException("Email is already in use");
        }

        String normalizedUsername = normalizeNullable(request.getUsername());
        if (normalizedUsername != null && userRepository.existsByUsername(normalizedUsername)) {
            throw new ValidationException("Username is already in use");
        }
    }

    private User findOrCreateGoogleUser(GoogleTokenVerifier.GoogleUserInfo googleUser) {
        return userRepository.findByEmail(googleUser.email())
                .map(existingUser -> {
                    existingUser.setGoogleAccountId(googleUser.googleAccountId());
                    if (existingUser.getFullName() == null || existingUser.getFullName().isBlank()) {
                        existingUser.setFullName(googleUser.fullName());
                    }
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(googleUser.email().trim().toLowerCase());
                    user.setFullName(googleUser.fullName());
                    user.setGoogleAccountId(googleUser.googleAccountId());
                    return userRepository.save(user);
                });
    }

    private AuthResponse buildAuthResponse(User user) {
        return new AuthResponse(
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user),
                userMapper.toResponse(user)
        );
    }

    private String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    private String generateResetToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            builder.append(String.format("%02x", value));
        }
        return builder.toString();
    }

    private String normalizeNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
