package com.example.messenger.service;

import com.example.messenger.domain.Message;
import com.example.messenger.domain.User;
import com.example.messenger.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public UserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public boolean makeAuthentication(String username, String password) {
        Optional<User> userById = userJpaRepository.findById(username);
        if (!userById.isPresent()) throw new RuntimeException("User not found");
        if (!userById.get().getPassword().equals(password)) throw new RuntimeException("Authentication error");
        return true;
    }

    public List<Message> retrieveMessages(String username, int amountOfRetrieveMessages) {
        Optional<User> userById = userJpaRepository.findById(username);
        if (!userById.isPresent()) throw new RuntimeException("User not found");
        int amountOfAllMessages = userById.get().getMessages().size();
        if (amountOfAllMessages-amountOfRetrieveMessages < 0) throw new RuntimeException("there are no so mush messages");
        return userById.get().getMessages().stream().skip(amountOfAllMessages-amountOfRetrieveMessages).collect(Collectors.toList());
    }

    @Transactional
    public void saveMessageForUser(String username, String message) {
        Optional<User> userById = userJpaRepository.findById(username);
        if (!userById.isPresent()) throw new RuntimeException("User not found");
        Message sendMessage = new Message();
        sendMessage.setMessage(message);
        sendMessage.setUser(userById.get());
        userById.get().getMessages().add(sendMessage);
        userJpaRepository.saveAndFlush(userById.get());
    }
}
