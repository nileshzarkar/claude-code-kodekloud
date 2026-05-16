package com.auth.dto;

public class UserInfoResponse {
    public Long userId;
    public String email;
    public boolean isVerified;

    public UserInfoResponse(Long userId, String email, boolean isVerified) {
        this.userId = userId;
        this.email = email;
        this.isVerified = isVerified;
    }
}
