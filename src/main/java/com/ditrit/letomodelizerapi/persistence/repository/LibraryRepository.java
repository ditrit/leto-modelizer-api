package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for the library repository that extends JpaRepository to handle data access operations for
 * {@code Library} entities. This repository interface provides CRUD operations and additional methods
 * to interact with the underlying library storage mechanism.
 *
 * @see JpaRepository
 */
public interface LibraryRepository extends JpaRepository<Library, Long> {

    /**
     * Checks if a library with the specified URL already exists in the database.
     *
     * @param url the URL of the library to check for existence
     * @return true if a library with the given URL exists, false otherwise
     */
    boolean existsByUrl(String url);

    /**
     * Checks if a library exists with the specified URL and a different ID.
     * This method is typically used to validate that the URL for a library is unique among all libraries
     * except for the library currently being updated. It helps prevent duplicate URLs in the system while
     * allowing updates to the existing library's other fields without changing its URL.
     *
     * @param url the URL of the library to check for uniqueness
     * @param id the ID of the library being updated, to exclude from the uniqueness check
     * @return true if a library with the given URL exists and its ID is not the one provided, false otherwise
     */
    boolean existsByUrlAndIdIsNot(String url, Long id);

    /**
     * Retrieves a page of {@code Library} entities that match the given specification.
     * This method allows for complex queries and filtering of library records using the provided
     * specification.
     *
     * @param specification a {@code Specification<Library>} object that defines the conditions for filtering
     *                      library records
     * @param pageable a {@code Pageable} object that defines the pagination parameters
     * @return a {@code Page<Library>} containing {@code Library} entities that match the given
     * specification
     */
    Page<Library> findAll(Specification<Library> specification, Pageable pageable);
}
