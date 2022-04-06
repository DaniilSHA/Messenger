package com.example.messenger.controllers;

import com.example.messenger.domain.Message;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'first message'"})
    public void WhenTryToSaveMessageThenReturnStatusOkTest (String username, String message) throws Exception {
        String postBody = "{\"name\":\"" + username + "\", \"message\":\"" + message + "\"}";
        mockMvc.perform(post("/message/send").
                        contentType(MediaType.APPLICATION_JSON).
                        content(postBody).
                        header("Token", "bearer_eyJhbGciOiJub25lIn0.eyJzdWIiOiJQZXRyIn0.")).
                andExpect(status().isOk());
    }


    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'history 2'"})
    public void WhenTryToRetrieveMessagesThenReturnStatusOkAndCorrectMessagesTest (String username, String message) throws Exception {

        List<Message> testMessages = new ArrayList<>();
        Message firstMessage = new Message();
        firstMessage.setMessage("first message");
        Message secondMessage = new Message();
        secondMessage.setMessage("second message");
        Collections.addAll(testMessages, firstMessage, secondMessage);

        Mockito.when(userService.retrieveMessages(username, 2)).thenReturn(testMessages);

        String postBody = "{\"name\":\"" + username + "\", \"message\":\"" + message + "\"}";
        String messages = mockMvc.perform(post("/message/send").
                        contentType(MediaType.APPLICATION_JSON).
                        content(postBody).
                        header("Token", "bearer_eyJhbGciOiJub25lIn0.eyJzdWIiOiJQZXRyIn0.")).
                andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Assertions.assertEquals("{\"messages\":[\"first message\",\"second message\"]}", messages);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|''"})
    public void WhenTryToSaveEmptyMessageThenReturnBadRequestTest (String username, String message) throws Exception {
        String postBody = "{\"name\":\"" + username + "\", \"message\":\"" + message + "\"}";
        String exceptionMessage = mockMvc.perform(post("/message/send").
                        contentType(MediaType.APPLICATION_JSON).
                        content(postBody).
                        header("Token", "bearer_eyJhbGciOiJub25lIn0.eyJzdWIiOiJQZXRyIn0.")).
                andExpect(status().isInternalServerError()).
                andReturn().
                getResponse().
                getContentAsString();
        Assertions.assertEquals("message could not be empty", exceptionMessage);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'first message'"})
    public void WhenTryToSaveOrRetrieveMessageWithWrongTokenThenReturnBadRequestTest (String username, String message) throws Exception {
        String postBody = "{\"name\":\"" + username + "\", \"message\":\"" + message + "\"}";
        String exceptionMessage = mockMvc.perform(post("/message/send").
                        contentType(MediaType.APPLICATION_JSON).
                        content(postBody).
                        header("Token", "bearer_WRONGTOKEN")).
                andExpect(status().isInternalServerError()).
                andReturn().
                getResponse().
                getContentAsString();
        Assertions.assertEquals("wrong token", exceptionMessage);
    }
}
