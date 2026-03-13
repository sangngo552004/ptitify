package com.sangngo552004.musicapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {

    @NotBlank(message = "refreshToken is required")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
