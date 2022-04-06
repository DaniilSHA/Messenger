package com.example.messenger.service;

import com.example.messenger.domain.Message;
import com.example.messenger.domain.User;
import com.example.messenger.repository.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserJpaRepository userJpaRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userJpaRepository);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'root'"})
    public void whenMakeAuthenticationWithCorrectUsernameAndPasswordThenReturnSuccessfulTest(String username, String password) {
        Optional<User> mockOptional = Optional.of(new User(username, password, new ArrayList<>()));
        Mockito.when(userJpaRepository.findById(username)).thenReturn(mockOptional);
        boolean resultOfAuthentication = userService.makeAuthentication(username, password);
        Assertions.assertTrue(resultOfAuthentication);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'root'"})
    public void whenMakeAuthenticationWithWrongUsernameAndPasswordThenThrowingRuntimeExceptionTest(String username, String password) {
        Optional<User> mockOptional = Optional.empty();
        Mockito.when(userJpaRepository.findById(username)).thenReturn(mockOptional);
        boolean throwingMessageTest = Assertions.assertThrows(RuntimeException.class, () -> userService.makeAuthentication(username, password)).
                getMessage().
                equals("User not found");
        Assertions.assertTrue(throwingMessageTest);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'admin'"})
    public void whenMakeAuthenticationWithUsernameAndWrongPasswordThenThrowingRuntimeExceptionTest(String username, String password) {
        Optional<User> mockOptional = Optional.of(new User(username, "root", new ArrayList<>()));
        Mockito.when(userJpaRepository.findById(username)).thenReturn(mockOptional);
        boolean throwingMessageTest = Assertions.assertThrows(RuntimeException.class, () -> userService.makeAuthentication(username, password)).
                getMessage().
                equals("Authentication error");
        Assertions.assertTrue(throwingMessageTest);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'3'"})
    public void whenRetrieveMessagesWithWrongUsernameThenThrowingRuntimeExceptionTest(String username, int amountOfRetrieveMessages) {
        Optional<User> mockOptional = Optional.empty();
        Mockito.when(userJpaRepository.findById(username)).thenReturn(mockOptional);
        boolean throwingMessageTest = Assertions.assertThrows(RuntimeException.class, () -> userService.retrieveMessages(username, amountOfRetrieveMessages)).
                getMessage().
                equals("User not found");
        Assertions.assertTrue(throwingMessageTest);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'2'"})
    public void whenRetrieveMessagesWithUsernameThenReturnCorrectResentMessagesTest(String username, int amountOfRetrieveMessages) {
        Message firstMessage = new Message();
        firstMessage.setMessage("first message");
        Message secondMessage = new Message();
        secondMessage.setMessage("second message");
        Message thirdMessage = new Message();
        thirdMessage.setMessage("third message");
        ArrayList<Message> allMessages = new ArrayList<>();
        Collections.addAll(allMessages, firstMessage, secondMessage, thirdMessage);

        Optional<User> mockOptional = Optional.of(new User(username, "root", allMessages));
        Mockito.when(userJpaRepository.findById(username)).thenReturn(mockOptional);
        List<Message> resultMessages = userService.retrieveMessages(username, amountOfRetrieveMessages);

        Assertions.assertEquals(2, resultMessages.size());
        Assertions.assertEquals(secondMessage, resultMessages.get(0));
        Assertions.assertEquals(thirdMessage, resultMessages.get(1));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'5'"})
    public void whenRetrieveMessagesWithUsernameAndExtraAmountOfMessagesThenThrowingRuntimeExceptionTest(String username, int amountOfRetrieveMessages) {
        Message firstMessage = new Message();
        firstMessage.setMessage("first message");
        Message secondMessage = new Message();
        secondMessage.setMessage("second message");
        Message thirdMessage = new Message();
        thirdMessage.setMessage("third message");
        ArrayList<Message> allMessages = new ArrayList<>();
        Collections.addAll(allMessages, firstMessage, secondMessage, thirdMessage);

        Optional<User> mockOptional = Optional.of(new User(username, "root", allMessages));
        Mockito.when(userJpaRepository.findById(username)).thenReturn(mockOptional);
        boolean throwingMessageTest = Assertions.assertThrows(RuntimeException.class, () -> userService.retrieveMessages(username, amountOfRetrieveMessages)).
                getMessage().
                equals("there are no so mush messages");
        Assertions.assertTrue(throwingMessageTest);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"'Petr'|'first message'"})
    public void whenSaveMessageForUserWithWrongUsernameThenThrowingRuntimeExceptionTest(String username, String message) {
        Optional<User> mockOptional = Optional.empty();
        Mockito.when(userJpaRepository.findById(username)).thenReturn(mockOptional);

        boolean throwingMessageTest = Assertions.assertThrows(RuntimeException.class, () -> userService.saveMessageForUser(username, message)).
                getMessage().
                equals("User not found");
        Assertions.assertTrue(throwingMessageTest);
    }
}
