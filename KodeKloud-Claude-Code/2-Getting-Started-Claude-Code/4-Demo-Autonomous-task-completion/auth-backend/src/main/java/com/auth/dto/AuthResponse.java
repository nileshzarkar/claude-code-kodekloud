package com.auth.dto;

public class AuthResponse {
    public String token;
    public String email;
    public Long userId;

    public AuthResponse(String token, String email, Long userId) {
        this.token = token;
        this.email = email;
        this.userId = userId;
    }
}
