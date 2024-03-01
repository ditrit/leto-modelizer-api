package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.config.Constants;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
     * This constant defines how the picture attribute is referenced within the application.
     */
    public static final String PICTURE_ATTRIBUTE = "picture";

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
    public User createAdmin(final String login) {
        return userRepository.findByLogin(login).orElseGet(() -> {
            log.info("Create user with login {}", login);
            User user = new User();
            user.setLogin(login);

            return userRepository.save(user);
        });
    }

    @Override
    public User getFromSession(final HttpSession session) {
        String login = session.getAttribute(Constants.DEFAULT_USER_PROPERTY).toString();

        return userRepository
            .findByLogin(login)
            .orElseThrow(() -> new ApiException(ErrorType.AUTHORIZATION_ERROR, Constants.DEFAULT_USER_PROPERTY, login));
    }

    @Override
    public HttpResponse<byte[]> getPicture(final User user) {
        if (StringUtils.isBlank(user.getPicture())) {
            throw new ApiException(ErrorType.EMPTY_VALUE, PICTURE_ATTRIBUTE);
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(user.getPicture()))
                .GET()
                .build();

            HttpResponse<byte[]> response = HttpClient
                .newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (!HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                throw new ApiException(ErrorType.WRONG_VALUE, PICTURE_ATTRIBUTE, user.getPicture());
            }

            return response;
        } catch (URISyntaxException | IOException e) {
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, PICTURE_ATTRIBUTE, user.getPicture());
        } catch (InterruptedException e) {
            // Re-interrupt the thread to restore the interrupt status
            Thread.currentThread().interrupt();
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, PICTURE_ATTRIBUTE, user.getPicture());
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
                .orElseThrow(() -> new ApiException(
                        ErrorType.ENTITY_NOT_FOUND,
                        Constants.DEFAULT_USER_PROPERTY,
                        login
                ));
    }

    @Override
    public void deleteByLogin(final String login) {
        User user =  this.findByLogin(login);

        this.userRepository.delete(user);
    }
}
