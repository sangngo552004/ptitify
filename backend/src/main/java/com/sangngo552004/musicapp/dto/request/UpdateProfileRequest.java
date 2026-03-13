package com.sangngo552004.musicapp.dto.request;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(min = 2, max = 120, message = "fullName must be between 2 and 120 characters")
    private String fullName;

    @Size(min = 8, max = 255, message = "password must be between 8 and 255 characters")
    private String password;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
