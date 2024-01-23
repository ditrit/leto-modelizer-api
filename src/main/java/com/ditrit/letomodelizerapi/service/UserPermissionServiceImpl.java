package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
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
     * The UserPermissionRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserPermission entities, such as querying,
     * saving and updating user data.
     */
    private UserPermissionRepository userPermissionRepository;

    @Override
    public List<UserPermission> getAllPermissions(final User user) {
        return userPermissionRepository.findAllByUserIdAndEntityIsNot(user.getId(), "LIBRARY");
    }

    @Override
    public void checkPermission(
            final User user,
            final String field,
            final EntityPermission entity,
            final ActionPermission action) {
        Map<String, String> filters = new HashMap<>();
        filters.put("userId", user.getId().toString());
        filters.put("entity", entity.name());
        filters.put("action", action.name());

        if (!userPermissionRepository.exists(new SpecificationHelper<>(UserPermission.class, filters))) {
            throw new ApiException(ErrorType.ENTITY_NOT_FOUND, field);
        }
    }

    @Override
    public void checkIsAdmin(final User user, final String field) {
        this.checkPermission(user, field, EntityPermission.ADMIN, ActionPermission.ACCESS);
    }
}
