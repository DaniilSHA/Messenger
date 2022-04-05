package com.example.messenger.controllers;

import com.example.messenger.domain.Message;
import com.example.messenger.dto.MessageRequest;
import com.example.messenger.dto.MessageResponse;
import com.example.messenger.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private UserService userService;

    @PutMapping("/send")
    public MessageResponse makeAuthentication(@RequestBody MessageRequest messageRequest, HttpServletRequest httpServletRequest) {
        String tokenFromHeader = httpServletRequest.getHeaders("Token").nextElement().split("_")[1];
        String tokenFromName = Jwts.builder().setSubject(messageRequest.getUsername()).compact();
        if (tokenFromHeader.equals(tokenFromName)) {
            String message = messageRequest.getMessage();
            if (message.trim().equals("")) throw new RuntimeException("message could not be empty");
            if (message.matches("history \\d+")) return giveMessagesFromHistory(messageRequest.getUsername(), message);
            else {
                userService.saveMessageForUser(messageRequest.getUsername(), messageRequest.getMessage());
                return null;
            }
        } else {
            throw new RuntimeException("wrong token");
        }
    }

    private MessageResponse giveMessagesFromHistory(String username, String message) {
        int amountOfRetrieveMessages = 0;
        try {
            amountOfRetrieveMessages = Integer.parseInt(message.split(" ")[1]);
        } catch (NumberFormatException ignored) {}
        List<Message> messages = userService.retrieveMessages(username, amountOfRetrieveMessages);
        return new MessageResponse(messages);
    }
}
