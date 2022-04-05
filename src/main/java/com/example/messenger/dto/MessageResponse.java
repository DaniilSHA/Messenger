package com.example.messenger.dto;

import com.example.messenger.domain.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class MessageResponse {
    List<String> messages;

    public MessageResponse(List<Message> messages) {
        this.messages = messages.stream().map(Message::getMessage).collect(Collectors.toList());
    }

    @JsonProperty
    public List<String> getMessages() {
        return messages;
    }
}
