package com.example.messenger.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticationRequest {
    private final String username;
    private final String password;

    @JsonCreator
    public AuthenticationRequest(@JsonProperty("name") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }
}

