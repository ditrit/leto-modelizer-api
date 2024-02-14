package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the PermissionService interface.
 *
 * <p>This class provides concrete implementations for the user management operations defined in PermissionService.
 * PermissionServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionServiceImpl implements PermissionService {

    /**
     * The PermissionRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to Permission entities, such as querying,
     * saving and updating user data.
     */
    private PermissionRepository permissionRepository;

    /**
     * Service to manage permission of access control.
     */
    private AccessControlPermissionService accessControlPermissionService;

    @Override
    public Permission findById(final Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "permissionId", id.toString()));
    }

    @Override
    public void createLibraryPermissions(final Library library, final AccessControl role) {
        Permission accessPermission = create(EntityPermission.LIBRARY, ActionPermission.ACCESS, library.getId());
        Permission deletePermission = create(EntityPermission.LIBRARY, ActionPermission.DELETE, library.getId());
        Permission updatePermission = create(EntityPermission.LIBRARY, ActionPermission.UPDATE, library.getId());

        if (role != null) {
            accessControlPermissionService.associate(role.getId(), accessPermission.getId());
            accessControlPermissionService.associate(role.getId(), deletePermission.getId());
            accessControlPermissionService.associate(role.getId(), updatePermission.getId());
        }
    }

    /**
     * Creates a new permission in the system for a specific entity, action, and library, and associates it
     * with the super administrator role. This method constructs a Permission object, sets the entity,
     * action, and library ID based on the provided parameters, saves the permission to the repository,
     * and then associates the permission with the super administrator role by its constant role ID.
     *
     * @param entity the type of entity the permission applies to, represented by the EntityPermission enum
     * @param action the type of action allowed by the permission, represented by the ActionPermission enum
     * @param libraryId the ID of the library to which the permission is specifically applied, if applicable
     * @return the saved Permission object, now associated with the super administrator role and persisted in the system
     */
    public Permission create(final EntityPermission entity, final ActionPermission action, final Long libraryId) {
        Permission permission = new Permission();

        permission.setAction(action.name());
        permission.setEntity(entity.name());
        permission.setLibraryId(libraryId);

        permission = permissionRepository.save(permission);
        accessControlPermissionService.associate(Constants.SUPER_ADMINISTRATOR_ROLE_ID, permission.getId());

        return permission;
    }
}
