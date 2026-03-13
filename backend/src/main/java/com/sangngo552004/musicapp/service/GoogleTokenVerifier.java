package com.sangngo552004.musicapp.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sangngo552004.musicapp.exception.AuthException;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@ApplicationScoped
public class GoogleTokenVerifier {

    public GoogleUserInfo verify(String googleIdToken) {
        try {
            GoogleIdTokenVerifier.Builder builder = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            );

            String googleClientId = readConfig("GOOGLE_CLIENT_ID");
            if (googleClientId != null) {
                builder.setAudience(Collections.singletonList(googleClientId));
            }

            GoogleIdToken token = builder.build().verify(googleIdToken);
            if (token == null) {
                throw new AuthException("Google token is invalid");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            return new GoogleUserInfo(
                    payload.getSubject(),
                    payload.getEmail(),
                    (String) payload.get("name")
            );
        } catch (GeneralSecurityException | IOException ex) {
            throw new AuthException("Unable to verify Google token");
        }
    }

    private String readConfig(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return value == null || value.isBlank() ? null : value;
    }

    public record GoogleUserInfo(String googleAccountId, String email, String fullName) {
    }
}
