package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.user.UserRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

import java.net.http.HttpResponse;
import java.util.List;
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
     * Creates an administrator user with the specified login.
     * This method is responsible for creating a new user entity with administrative privileges based on the provided
     * login.
     * The created user is granted administrator rights, making them capable of performing administrative tasks within
     * the application.
     * This function is typically used during application initialization or setup processes to ensure that at least one
     * administrator exists in the system.
     *
     * @param login the login identifier for the new administrator user.
     * @return the created User entity with administrator privileges.
     */
    User createAdmin(String login);

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
     * @param filters a Map of strings representing the filtering criteria.
     * @param queryFilter a Pageable object for pagination information.
     * @return A {@link Page<User>} containing users that match the filtering criteria.
     */
    Page<User> findAll(Map<String, List<String>> filters, QueryFilter queryFilter);

    /**
     * Retrieves a user by their login identifier.
     * This method is used to find a user based on their unique login.
     *
     * @param login The login identifier of the user to be retrieved.
     * @return The User object if found, or {@code null} if no user exists with the given login.
     */
    User findByLogin(String login);

    /**
     * Deletes a user by their login identifier.
     * This method is used to delete a user based on their unique login.
     *
     * @param login The login identifier of the user to be retrieved.
     */
    void deleteByLogin(String login);
}
