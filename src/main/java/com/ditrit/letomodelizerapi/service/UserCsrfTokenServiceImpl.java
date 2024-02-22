package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;
import com.ditrit.letomodelizerapi.persistence.repository.UserCsrfTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserCsrfTokenService interface.
 *
 * This class provides concrete implementations for the user management operations defined in UserCsrfTokenService.
 * UserCsrfTokenServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserCsrfTokenServiceImpl implements UserCsrfTokenService {

    /**
     * Repository for CRUD operations on UserCsrfToken entities.
     */
    private final UserCsrfTokenRepository userCsrfTokenRepository;

    @Override
    public UserCsrfToken findByLogin(final String login) {
        return userCsrfTokenRepository.findByLogin(login)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "token"));
    }
}
