package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AIConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface for the AI configuration repository that extends JpaRepository to handle data access operations for
 * {@code AIConfiguration} entities. This repository interface provides CRUD operations and additional methods
 * to interact with the underlying AI configuration storage mechanism.
 *
 * @see JpaRepository
 */
public interface AIConfigurationRepository extends JpaRepository<AIConfiguration, UUID> {

    /**
     * Retrieves a page of AIConfiguration entities that match the given specification.
     * This method allows for complex queries and filtering of AIConfiguration records using the provided
     * specification.
     *
     * @param specification a Specification object that defines the conditions for filtering AIConfiguration
     *                      records.
     * @param pageable a Pageable object that defines the pagination parameters.
     * @return a Page containing AIConfiguration entities that match the given specification.
     */
    Page<AIConfiguration> findAll(Specification<AIConfiguration> specification, Pageable pageable);

    /**
     * Checks whether a AIConfiguration entity with the given key exists in the repository.
     * This method allows for verifying the existence of a AIConfiguration by its unique key.
     *
     * @param handler a String representing the handler of the AIConfiguration entity.
     * @param key a String representing the key of the AIConfiguration entity.
     * @return true if a AIConfiguration with the given key exists, false otherwise.
     */
    boolean existsByHandlerAndKey(String handler, String key);

    /**
     * Retrieves an Optional containing a AIConfiguration entity with the given ID.
     * This method looks for a AIConfiguration by its unique UUID and returns it if found.
     *
     * @param id the UUID of the AIConfiguration entity.
     * @return an Optional containing the found AIConfiguration entity, or an empty Optional if no entity
     *         with the given ID is found.
     */
    Optional<AIConfiguration> findById(UUID id);
}
