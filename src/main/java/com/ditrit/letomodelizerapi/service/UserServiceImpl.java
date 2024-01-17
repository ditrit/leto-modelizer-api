package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.user.UserRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of the UserService interface.
 *
 * This class provides concrete implementations for the user management operations defined in UserService.
 * UserServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    /**
     * The UserRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to User entities, such as querying, saving,
     * and updating user data.
     */
    private UserRepository userRepository;

    @Override
    public User save(final UserRecord userRecord) {
        User user = userRepository.findByLogin(userRecord.login())
            .orElse(new User());

        user.setEmail(userRecord.email());
        user.setLogin(userRecord.login());
        user.setName(userRecord.name());
        user.setPicture(userRecord.picture());

        return userRepository.save(user);
    }

    @Override
    public User getFromSession(final HttpSession session) {
        String login = session.getAttribute("login").toString();

        return userRepository
            .findByLogin(login)
            .orElseThrow(() -> new ApiException(ErrorType.AUTHORIZATION_ERROR, "login", login));
    }

    @Override
    public HttpResponse<byte[]> getPicture(final User user) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(user.getPicture()))
                .GET()
                .build();

            return HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (URISyntaxException | IOException e) {
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, "picture", user.getPicture());
        } catch (InterruptedException e) {
            // Re-interrupt the thread to restore the interrupt status
            Thread.currentThread().interrupt();
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, "picture", user.getPicture());
        }
    }
}
