package com.yejunlong.model;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String reason;

    public Boolean isSuccess() {
        return token != null&&token.isEmpty();
    }
}
