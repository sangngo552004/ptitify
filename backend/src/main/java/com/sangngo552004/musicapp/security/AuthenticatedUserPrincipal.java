package com.sangngo552004.musicapp.security;

import java.security.Principal;

public class AuthenticatedUserPrincipal implements Principal {

    private final Long userId;
    private final String email;

    public AuthenticatedUserPrincipal(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return email;
    }
}
