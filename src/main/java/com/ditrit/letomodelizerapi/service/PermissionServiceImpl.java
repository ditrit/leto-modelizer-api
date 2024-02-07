package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
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

    @Override
    public Permission findById(final Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "permissionId", id.toString()));
    }
}
