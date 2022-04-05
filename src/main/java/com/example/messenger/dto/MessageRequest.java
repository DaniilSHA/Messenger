package com.example.messenger.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageRequest {

    private final String username;
    private final String message;

    @JsonCreator
    public MessageRequest(@JsonProperty("name") String username, @JsonProperty("message") String password) {
        this.username = username;
        this.message = password;
    }
}
