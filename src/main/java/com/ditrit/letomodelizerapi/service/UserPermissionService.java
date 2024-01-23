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
}
