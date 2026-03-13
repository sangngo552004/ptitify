package com.sangngo552004.musicapp.security;

import com.sangngo552004.musicapp.entity.User;
import com.sangngo552004.musicapp.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@ApplicationScoped
public class JwtProvider {

    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private SecretKey secretKey;
    private String issuer;
    private long accessTokenMinutes;
    private long refreshTokenDays;

    @PostConstruct
    public void init() {
        String secret = readConfig("JWT_SECRET",
                "change-this-jwt-secret-for-production-change-this-jwt-secret");
        issuer = readConfig("JWT_ISSUER", "music-app");
        accessTokenMinutes = Long.parseLong(readConfig("JWT_ACCESS_TOKEN_MINUTES", "15"));
        refreshTokenDays = Long.parseLong(readConfig("JWT_REFRESH_TOKEN_DAYS", "7"));
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS_TOKEN_TYPE, Instant.now().plus(accessTokenMinutes, ChronoUnit.MINUTES));
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFRESH_TOKEN_TYPE, Instant.now().plus(refreshTokenDays, ChronoUnit.DAYS));
    }

    public Claims parseAccessToken(String token) {
        Claims claims = parse(token);
        validateTokenType(claims, ACCESS_TOKEN_TYPE);
        return claims;
    }

    public Claims parseRefreshToken(String token) {
        Claims claims = parse(token);
        validateTokenType(claims, REFRESH_TOKEN_TYPE);
        return claims;
    }

    private String generateToken(User user, String tokenType, Instant expiry) {
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuer(issuer)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiry))
                .claim("type", tokenType)
                .claim("email", user.getEmail())
                .signWith(secretKey)
                .compact();
    }

    private Claims parse(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception ex) {
            throw new AuthException("Invalid or expired token");
        }
    }

    private void validateTokenType(Claims claims, String expectedType) {
        String type = claims.get("type", String.class);
        if (!expectedType.equals(type)) {
            throw new AuthException("Token type is invalid");
        }
    }

    private String readConfig(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
