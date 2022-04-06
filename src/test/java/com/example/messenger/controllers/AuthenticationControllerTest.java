package com.example.messenger.controllers;

import com.example.messenger.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Natali'|'admin'"})
    public void WhenTryMakeAuthenticationThenReturnCorrectTokenTest(String username, String password) throws Exception {
        String postBody = "{\"name\":\"" + username + "\", \"password\":\"" + password + "\"}";
        String returnValue = mockMvc.perform(post("/user/authentication").
                        contentType(MediaType.APPLICATION_JSON).
                        content(postBody)).
                andExpect(status().isOk()).
                andReturn().
                getResponse().
                getContentAsString();
        Assertions.assertEquals("{\"token\":\"eyJhbGciOiJub25lIn0.eyJzdWIiOiJOYXRhbGkifQ.\"}", returnValue);
    }
}
