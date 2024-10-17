package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AISecret;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface for the AI secret repository that extends JpaRepository to handle data access operations for
 * {@code AISecret} entities. This repository interface provides CRUD operations and additional methods
 * to interact with the underlying AI secret storage mechanism.
 *
 * @see JpaRepository
 */
public interface AISecretRepository extends JpaRepository<AISecret, UUID> {

    /**
     * Retrieves a page of AISecret entities that match the given specification.
     * This method allows for complex queries and filtering of AISecret records using the provided
     * specification.
     *
     * @param specification a Specification object that defines the conditions for filtering AISecret
     *                      records.
     * @param pageable a Pageable object that defines the pagination parameters.
     * @return a Page containing AISecret entities that match the given specification.
     */
    Page<AISecret> findAll(Specification<AISecret> specification, Pageable pageable);

    /**
     * Checks whether a AISecret entity with the given key exists in the repository.
     * This method allows for verifying the existence of a AISecret by its unique key.
     *
     * @param key a String representing the unique key of the AISecret entity.
     * @return true if a AISecret with the given key exists, false otherwise.
     */
    boolean existsByKey(String key);

    /**
     * Retrieves an Optional containing a AISecret entity with the given ID.
     * This method looks for a AISecret by its unique UUID and returns it if found.
     *
     * @param id the UUID of the AISecret entity.
     * @return an Optional containing the found AISecret entity, or an empty Optional if no entity
     *         with the given ID is found.
     */
    Optional<AISecret> findById(UUID id);
}
