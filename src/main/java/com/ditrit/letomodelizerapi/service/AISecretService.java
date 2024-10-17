package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AISecretRecord;
import com.ditrit.letomodelizerapi.persistence.model.AISecret;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

/**
 * Service interface for managing AISecret entities.
 * This interface defines methods for operations like finding, creating, updating, and deleting
 * AISecret entities.
 */
public interface AISecretService {
    /**
     * Finds and returns a page of AISecret entities, filtered by provided criteria.
     *
     * @param filters  a Map of strings representing the filtering criteria.
     * @param pageable a Pageable object for pagination information.
     * @return a Page of AISecret entities matching the specified type and filters.
     */
    Page<AISecret> findAll(Map<String, String> filters, Pageable pageable);

    /**
     * Finds and returns an AISecret entity of a specific type by its ID.
     *
     * @param id   the ID of the AISecret entity.
     * @return the found AISecret entity, or null if no entity is found with the given ID.
     */
    AISecret findById(UUID id);

    /**
     * Creates a new AISecret entity of a specified type.
     *
     * @param aiSecretRecord an AISecretRecord object containing the data for the new entity.
     * @return the newly created AISecret entity.
     */
    AISecret create(AISecretRecord aiSecretRecord);

    /**
     * Updates an existing AISecret entity of a specified type and ID.
     *
     * @param id                        the ID of the AISecret entity to update.
     * @param aiSecretRecord a AISecretRecord object containing the updated data.
     * @return the updated AISecret entity.
     */
    AISecret update(UUID id, AISecretRecord aiSecretRecord);

    /**
     * Deletes an AISecret entity of a specified type by its ID.
     *
     * @param id   the ID of the AISecret entity to delete.
     */
    void delete(UUID id);
}
