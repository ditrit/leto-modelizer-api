package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;

/**
 * The UserService interface defines the operations for managing user csrf token within the application.
 */
public interface UserCsrfTokenService {

    /**
     * Retrieves a user csrf token by their login identifier.
     * This method is used to find a user csrf token based on their unique login.
     *
     * @param login The login identifier of the user csrf token to be retrieved.
     * @return The UserCsrfToken object if found, or {@code null} if no user exists with the given login.
     */
    UserCsrfToken findByLogin(String login);
}
