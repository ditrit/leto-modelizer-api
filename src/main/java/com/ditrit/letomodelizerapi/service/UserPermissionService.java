package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserPermission;

import java.util.List;

/**
 * The UserPermissionService interface defines the operations for managing permissions of the current user within
 * the application.
 */
public interface UserPermissionService {

    /**
     * Retrieves all permissions assigned to a specified user.
     * Only retrieve permissions used in Leto-modelizer, Library permissions are excluded here.
     *
     * @param user The user whose permissions are to be retrieved.
     * @return A list of {@link UserPermission} objects representing the permissions of the user.
     */
    List<UserPermission> getAllPermissions(User user);

    /**
     * Checks if a given user has permission for a specific action on an entity.
     * This method determines whether the specified user is authorized to perform the given action
     * (e.g., READ, WRITE) on the specified entity (e.g., LIBRARY, DOCUMENT). The check is typically
     * based on examining the user's roles and the permissions associated with those roles.
     *
     * @param user the user whose permissions are being checked
     * @param entity the type of entity on which the action is to be performed, represented by the EntityPermission enum
     * @param action the action to be checked, represented by the ActionPermission enum
     * @return true if the user has the specified permission, false otherwise
     */
    boolean hasPermission(User user, EntityPermission entity, ActionPermission action);

    /**
     * Checks if a user has a specific permission for a given field, entity, and action.
     * Throws a security exception if the user does not have the required permission.
     *
     * @param user The user whose permission is to be checked.
     * @param field The field to set in case of security exception.
     * @param entity The type of entity for which the permission is required.
     * @param action The type of action for which the permission is required.
     */
    void checkPermission(User user, String field, EntityPermission entity, ActionPermission action);
    /**
     * Checks if a user has administrative privileges for a specific field.
     * Throws a security exception if the user is not an admin for the specified field.
     * This method internally uses {@link #checkPermission(User, String, EntityPermission, ActionPermission)}
     *
     * @param user The user whose admin status is to be checked.
     * @param field The field to set in case of security exception.
     */
    void checkIsAdmin(User user, String field);

    /**
     * Verifies if a user has the specified action permission on a library identified by its ID.
     * This method checks the user's permissions to determine if they are authorized to perform the given action
     * (e.g., ACCESS, CREATE, DELETE) on the library specified by the ID. If the user does not have the necessary
     * permissions, this method should throw an exception or handle the authorization failure according to the
     * application's security policy.
     *
     * @param user the user whose permission is being checked
     * @param action the action for which permission is being verified, represented by the ActionPermission enum
     * @param id the ID of the library on which the action is attempted
     * @throws SecurityException or a custom application-specific exception if the user lacks the required permission
     */
    void checkLibraryPermission(User user, ActionPermission action, Long id);
}
