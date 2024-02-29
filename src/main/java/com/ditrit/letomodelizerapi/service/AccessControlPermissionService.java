package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermissionView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

/**
 * Service interface for managing AccessControl entities.
 * This interface defines methods for operations like finding, creating, updating, and deleting AccessControl entities.
 */
public interface AccessControlPermissionService {

    /**
     * Finds and returns a page of AccessControl entities of a specific type, filtered by provided criteria.
     *
     * @param id       the ID of the AccessControl entity
     * @param filters  a Map of strings representing the filtering criteria
     * @param pageable a Pageable object for pagination information
     * @return a Page of AccessControl entities matching the specified type and filters
     */
    Page<AccessControlPermissionView> findAll(UUID id, Map<String, String> filters, Pageable pageable);

    /**
     * Associates a specific permission with an AccessControl entity. This method creates a new linkage between the
     * AccessControl entity identified by {@code id} and the permission identified by {@code permissionId}, effectively
     * granting the specified permission to the access control.
     *
     * @param id           the ID of the AccessControl entity to which the permission will be associated
     * @param permissionId the ID of the permission to associate with the AccessControl entity
     */
    void associate(UUID id, UUID permissionId);

    /**
     * Dissociates a specific permission from an AccessControl entity. This method removes an existing linkage between
     * the AccessControl entity identified by {@code id} and the permission identified by {@code permissionId},
     * effectively revoking the specified permission from the access control.
     *
     * @param id           the ID of the AccessControl entity from which the permission will be dissociated
     * @param permissionId the ID of the permission to dissociate from the AccessControl entity
     */
    void dissociate(UUID id, UUID permissionId);
}
