package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for UserCsrfToken entities.
 * This interface extends JpaRepository, providing standard CRUD operations for UserCsrfToken entities,
 * along with additional methods to find and delete a UserCsrfToken by the user's login.
 *
 * It enables the handling of CSRF (Cross-Site Request Forgery) protection tokens associated with user sessions,
 * facilitating the secure management of these tokens in the application's persistence layer.
 */
public interface UserCsrfTokenRepository extends JpaRepository<UserCsrfToken, Long> {

    /**
     * Finds a UserCsrfToken entity based on the user's login.
     * This method allows for retrieving the CSRF protection token assigned to a specific user, identified by their
     * login.
     *
     * @param login the login identifier of the user whose CSRF token is being retrieved.
     * @return an Optional containing the found UserCsrfToken, or an empty Optional if no token is found for the given
     * login.
     */
    Optional<UserCsrfToken> findByLogin(String login);

    /**
     * Deletes a UserCsrfToken entity based on the user's login.
     * This method facilitates the removal of a user's CSRF protection token from the database,
     * typically used when a user logs out or when their session is invalidated.
     *
     * @param login the login identifier of the user whose CSRF token is to be deleted.
     */
    void deleteByLogin(String login);
}
