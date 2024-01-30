package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Service interface for managing AccessControl entities.
 * This interface defines methods for operations like finding, creating, updating, and deleting AccessControl entities.
 */
public interface AccessControlService {

    /**
     * Finds and returns a page of AccessControl entities of a specific type, filtered by provided criteria.
     *
     * @param type     the AccessControlType to filter the AccessControl entities
     * @param filters  a Map of strings representing the filtering criteria
     * @param pageable a Pageable object for pagination information
     * @return a Page of AccessControl entities matching the specified type and filters
     */
    Page<AccessControl> findAll(AccessControlType type, Map<String, String> filters, Pageable pageable);

    /**
     * Finds and returns an AccessControl entity of a specific type by its ID.
     *
     * @param type the AccessControlType of the AccessControl entity
     * @param id   the ID of the AccessControl entity
     * @return the found AccessControl entity, or null if no entity is found with the given ID
     */
    AccessControl findById(AccessControlType type, Long id);

    /**
     * Creates a new AccessControl entity of a specified type.
     *
     * @param type                the AccessControlType of the new AccessControl entity
     * @param accessControlRecord an AccessControlRecord object containing the data for the new entity
     * @return the newly created AccessControl entity
     */
    AccessControl create(AccessControlType type, AccessControlRecord accessControlRecord);

    /**
     * Updates an existing AccessControl entity of a specified type and ID.
     *
     * @param type                the AccessControlType of the AccessControl entity to update
     * @param id                  the ID of the AccessControl entity to update
     * @param accessControlRecord an AccessControlRecord object containing the updated data
     * @return the updated AccessControl entity
     */
    AccessControl update(AccessControlType type, Long id, AccessControlRecord accessControlRecord);

    /**
     * Deletes an AccessControl entity of a specified type by its ID.
     *
     * @param type the AccessControlType of the AccessControl entity to delete
     * @param id   the ID of the AccessControl entity to delete
     */
    void delete(AccessControlType type, Long id);
}
