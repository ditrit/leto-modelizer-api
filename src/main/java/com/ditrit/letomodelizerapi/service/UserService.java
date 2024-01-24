package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.user.UserRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.http.HttpResponse;
import java.util.Map;

/**
 * The UserService interface defines the operations for managing user data within the application.
 */
public interface UserService {

    /**
     * Saves or updates a user in the database. If a user with the provided email already exists,
     * it updates their information; otherwise, it creates a new user record.
     *
     * @param user The UserRecord object containing the user information to be saved.
     * @return The updated or newly created User object.
     */
    User save(UserRecord user);

    /**
     * Retrieves the user associated with the current session.
     *
     * @param session The HttpSession from which the user is to be retrieved.
     * @return The User object retrieved from the session.
     */
    User getFromSession(HttpSession session);

    /**
     * Provides the user's profile picture as a byte array encapsulated in an HttpResponse object.
     * This method is useful for retrieving the picture data for display or processing.
     *
     * @param user The User object for which the profile picture is requested.
     * @return An HttpResponse object containing the byte array of the user's profile picture.
     */
    HttpResponse<byte[]> getPicture(User user);

    /**
     * Retrieves a paginated list of users that match the given filter criteria.
     * This method allows for dynamic querying of users based on specified filters
     * and includes pagination and sorting functionality.
     *
     * @param filters A map of string key-value pairs representing the filtering criteria.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page<User>} containing users that match the filtering criteria.
     */
    Page<User> findAll(Map<String, String> filters, Pageable pageable);

    /**
     * Retrieves a user by their login identifier.
     * This method is used to find a user based on their unique login.
     *
     * @param login The login identifier of the user to be retrieved.
     * @return The User object if found, or {@code null} if no user exists with the given login.
     */
    User findByLogin(String login);
}
