package com.sangngo552004.musicapp.security;

import com.sangngo552004.musicapp.entity.User;
import com.sangngo552004.musicapp.exception.AuthException;
import com.sangngo552004.musicapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private JwtProvider jwtProvider;

    @Inject
    private UserRepository userRepository;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException("Missing Bearer token");
        }

        String token = authorization.substring("Bearer ".length()).trim();
        Claims claims = jwtProvider.parseAccessToken(token);
        Long userId = Long.parseLong(claims.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Authenticated user was not found"));

        AuthenticatedUserPrincipal principal = new AuthenticatedUserPrincipal(user.getId(), user.getEmail());
        boolean secure = requestContext.getSecurityContext() != null && requestContext.getSecurityContext().isSecure();
        requestContext.setSecurityContext(new AppSecurityContext(principal, secure));
    }
}
