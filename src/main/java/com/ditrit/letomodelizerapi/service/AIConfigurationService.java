package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.ai.AIConfigurationRecord;
import com.ditrit.letomodelizerapi.persistence.model.AIConfiguration;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service interface for managing AIConfiguration entities.
 * This interface defines methods for operations like finding, creating, updating, and deleting
 * AIConfiguration entities.
 */
public interface AIConfigurationService {
    /**
     * Finds and returns a page of AIConfiguration entities, filtered by provided criteria.
     *
     * @param filters  a Map of strings representing the filtering criteria.
     * @param queryFilter a Pageable object for pagination information.
     * @return a Page of AIConfiguration entities matching the specified type and filters.
     */
    Page<AIConfiguration> findAll(Map<String, List<String>> filters, QueryFilter queryFilter);

    /**
     * Finds and returns an AIConfiguration entity of a specific type by its ID.
     *
     * @param id   the ID of the AIConfiguration entity.
     * @return the found AIConfiguration entity, or null if no entity is found with the given ID.
     */
    AIConfiguration findById(UUID id);

    /**
     * Creates a new AIConfiguration entity of a specified type.
     *
     * @param aiConfigurationRecord an AIConfigurationRecord object containing the data for the new entity.
     * @return the newly created AIConfiguration entity.
     */
    AIConfiguration create(AIConfigurationRecord aiConfigurationRecord);

    /**
     * Updates an existing AIConfiguration entity of a specified type and ID.
     *
     * @param id                        the ID of the AIConfiguration entity to update.
     * @param aiConfigurationRecord a AIConfigurationRecord object containing the updated data.
     * @return the updated AIConfiguration entity.
     */
    AIConfiguration update(UUID id, AIConfigurationRecord aiConfigurationRecord);

    /**
     * Deletes an AIConfiguration entity of a specified type by its ID.
     *
     * @param id   the ID of the AIConfiguration entity to delete.
     */
    void delete(UUID id);
}
