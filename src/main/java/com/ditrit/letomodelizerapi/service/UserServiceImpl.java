package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.user.UserRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.repository.UserRepository;
import com.ditrit.letomodelizerapi.persistence.specification.SpecificationHelper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

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
     * The attribute key used for retrieving the user's login information from a session or context.
     * This constant defines how the login attribute is referenced within the application.
     */
    private static final String LOGIN_ATTRIBUTE = "login";

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
        String login = session.getAttribute(LOGIN_ATTRIBUTE).toString();

        return userRepository
            .findByLogin(login)
            .orElseThrow(() -> new ApiException(ErrorType.AUTHORIZATION_ERROR, LOGIN_ATTRIBUTE, login));
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

    @Override
    public Page<User> findAll(final Map<String, String> filters, final Pageable pageable) {
        return this.userRepository.findAll(new SpecificationHelper<>(User.class, filters), PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))));
    }

    @Override
    public User findByLogin(final String login) {
        return this.userRepository.findByLogin(login)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, LOGIN_ATTRIBUTE, login));
    }
}
