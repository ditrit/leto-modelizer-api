package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.function.UserPermissionToPermissionFunction;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserPermission;
import com.ditrit.letomodelizerapi.persistence.repository.UserPermissionRepository;
import com.ditrit.letomodelizerapi.persistence.specification.SpecificationHelper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the UserPermissionService interface.
 *
 * This class provides concrete implementations for the user management operations defined in UserPermissionService.
 * UserPermissionServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserPermissionServiceImpl implements UserPermissionService {

    /**
     * Library id field name.
     */
    private static final String LIBRARY_ID = "libraryId";
    /**
     * The UserPermissionRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserPermission entities, such as querying,
     * saving and updating user data.
     */
    private UserPermissionRepository userPermissionRepository;

    @Override
    public List<Permission> getAllPermissions(final User user) {
        return userPermissionRepository.findAllByUserIdAndEntityIsNot(user.getId(), "LIBRARY")
                .stream().map(new UserPermissionToPermissionFunction()).toList();
    }

    @Override
    public boolean hasPermission(
            final User user,
            final EntityPermission entity,
            final ActionPermission action) {
        Map<String, String> filters = new HashMap<>();
        filters.put("userId", user.getId().toString());
        filters.put("entity", entity.name());
        filters.put("action", action.name());
        filters.put(LIBRARY_ID, null);

        return userPermissionRepository.exists(new SpecificationHelper<>(UserPermission.class, filters));
    }

    @Override
    public void checkPermission(
            final User user,
            final String field,
            final EntityPermission entity,
            final ActionPermission action) {
        if (!hasPermission(user, entity, action)) {
            throw new ApiException(ErrorType.ENTITY_NOT_FOUND, field);
        }
    }

    @Override
    public void checkIsAdmin(final User user, final String field) {
        this.checkPermission(user, field, EntityPermission.ADMIN, ActionPermission.ACCESS);
    }

    @Override
    public void checkLibraryPermission(final User user, final  ActionPermission action, final UUID id) {
        String libraryId = null;
        Map<String, String> filters = new HashMap<>();
        filters.put("userId", user.getId().toString());
        filters.put("entity", EntityPermission.LIBRARY.name());
        filters.put("action", action.name());

        if (id == null) {
            filters.put(LIBRARY_ID, "null");
        } else {
            libraryId = id.toString();
            filters.put(LIBRARY_ID, String.format("null|%s", libraryId));
        }

        if (userPermissionRepository.exists(new SpecificationHelper<>(UserPermission.class, filters))) {
            return;
        }

        if (ActionPermission.CREATE.equals(action)) {
            throw new ApiException(ErrorType.NO_VALID_PERMISSION, "library", "create");
        }

        throw new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", libraryId);
    }
}
