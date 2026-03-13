package com.sangngo552004.musicapp.security;

import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class AppSecurityContext implements SecurityContext {

    private final AuthenticatedUserPrincipal principal;
    private final boolean secure;

    public AppSecurityContext(AuthenticatedUserPrincipal principal, boolean secure) {
        this.principal = principal;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
