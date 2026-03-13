package com.sangngo552004.musicapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginRequest {

    @NotBlank(message = "googleIdToken is required")
    private String googleIdToken;

    public String getGoogleIdToken() {
        return googleIdToken;
    }

    public void setGoogleIdToken(String googleIdToken) {
        this.googleIdToken = googleIdToken;
    }
}
