package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface for the permission repository that extends JpaRepository to handle data access operations for
 * {@code Permission} entities. This repository interface provides CRUD operations and additional methods
 * to interact with the underlying permission storage mechanism.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    /**
     * Retrieves a page of AccessControl entities that match the given specification.
     * This method allows for complex queries and filtering of AccessControl records using the provided specification.
     *
     * @param specification a Specification object that defines the conditions for filtering AccessControl records
     * @param pageable a Pageable object that defines the pagination parameters
     * @return a Page containing AccessControl entities that match the given specification
     */
    Page<Permission> findAll(Specification<Permission> specification, Pageable pageable);

    /**
     * Retrieves a permission by its unique identifier.
     *
     * @param id the unique identifier of the permission to retrieve.
     * @return an {@code Optional<Permission>} containing the permission if found, or an empty {@code Optional} if no
     *         permission exists with the provided identifier.
     */
    Optional<Permission> findById(UUID id);
}
