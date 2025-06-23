package org.example.dto;

public class LoginResponse {
    private String token;
    private long expiresIn;
    private String tokenType;

    public LoginResponse(String token, long expiresIn, String tokenType) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }
}