package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for the template of library repository that extends JpaRepository to handle data access operations for
 * {@code LibraryTemplate} entities. This repository interface provides CRUD operations and additional methods
 * to interact with the underlying template of library storage mechanism.
 *
 * @see JpaRepository
 */
public interface LibraryTemplateRepository extends JpaRepository<LibraryTemplate, Long> {

    /**
     * Retrieves a page of {@code LibraryTemplate} entities that match the given specification.
     * This method allows for complex queries and filtering of library template records using the provided
     * specification.
     *
     * @param specification a {@code Specification<LibraryTemplate>} object that defines the conditions for filtering
     *                      library template records
     * @param pageable a {@code Pageable} object that defines the pagination parameters
     * @return a {@code Page<LibraryTemplate>} containing {@code LibraryTemplate} entities that match the given
     * specification
     */
    Page<LibraryTemplate> findAll(Specification<LibraryTemplate> specification, Pageable pageable);

    /**
     * Deletes all records associated with a specific library ID.
     * This method is designed to remove any entries from the database that are linked to the specified library,
     * ensuring that all references or dependencies on the library are cleanly removed. It is typically used in
     * the context of cleaning up data related to a library that is being deleted, to maintain database integrity
     * and prevent orphaned records.
     *
     * @param id the ID of the library for which all associated records need to be deleted
     */
    void deleteByLibraryId(Long id);
}
