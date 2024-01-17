package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.user.UserRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import jakarta.servlet.http.HttpSession;

import java.net.http.HttpResponse;

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
}
