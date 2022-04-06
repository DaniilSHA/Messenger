package com.example.messenger.controllers;

import com.example.messenger.dto.AuthenticationRequest;
import com.example.messenger.dto.AuthenticationResponse;
import com.example.messenger.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/authentication")
    public AuthenticationResponse makeAuthentication(@RequestBody AuthenticationRequest userRequest) {
        userService.makeAuthentication(userRequest.getUsername(), userRequest.getPassword());
        String token = Jwts.builder().setSubject(userRequest.getUsername()).compact();
        return new AuthenticationResponse(token);
    }
}
