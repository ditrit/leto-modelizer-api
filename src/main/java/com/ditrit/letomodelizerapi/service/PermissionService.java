package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.Permission;

/**
 * The PermissionService interface defines the operations for managing permissions within the application.
 */
public interface PermissionService {
    /**
     * Retrieves a {@link Permission} entity by its unique identifier.
     *
     * This method is responsible for fetching a permission from the persistence layer based on the provided ID.
     * It is typically used to obtain the details of a specific permission, including its name, associated actions,
     * and any other relevant attributes defined within the {@code Permission} entity.
     *
     * @param id the unique identifier of the permission to retrieve.
     * @return the {@link Permission} entity corresponding to the specified ID. If no permission is found with the
     *         given ID, this method may throw an exception or return {@code null}, depending on the implementation.
     *         It is recommended to document the specific behavior regarding not found entities in the implementation
     *         details.
     */
    Permission findById(Long id);

    /**
     * Creates permissions for a given library and assigns them to a specified role.
     * This method is responsible for setting up access control permissions for a library entity,
     * ensuring that the specified role has the appropriate permissions to access or manage the library.
     * The implementation details would typically involve associating the library with the role in a way
     * that permissions are enforceable according to the application's access control policies.
     *
     * @param library the library entity for which to create permissions
     * @param role the access control role to which the permissions will be assigned
     */
    void createLibraryPermissions(Library library, AccessControl role);
}
