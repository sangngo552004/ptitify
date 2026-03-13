package com.sangngo552004.musicapp.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailService {

    public void sendPasswordResetEmail(String email, String token) {
        // Mock email delivery for local development.
        System.out.printf("Mock password reset email sent to %s with token: %s%n", email, token);
    }
}
