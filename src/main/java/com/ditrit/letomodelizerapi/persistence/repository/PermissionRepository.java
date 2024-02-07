package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface for the permission repository that extends JpaRepository to handle data access operations for
 * {@code Permission} entities. This repository interface provides CRUD operations and additional methods
 * to interact with the underlying permission storage mechanism.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface PermissionRepository extends JpaRepository<Permission, String> {

    /**
     * Retrieves a permission by its unique identifier.
     *
     * @param id the unique identifier of the permission to retrieve.
     * @return an {@code Optional<Permission>} containing the permission if found, or an empty {@code Optional} if no
     *         permission exists with the provided identifier.
     */
    Optional<Permission> findById(Long id);
}
