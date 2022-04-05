package com.example.messenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    private final String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }
}
