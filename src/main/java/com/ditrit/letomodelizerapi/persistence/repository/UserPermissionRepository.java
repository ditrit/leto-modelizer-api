package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserPermission;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * UserRepository interface extends JpaRepository for handling data persistence operations for User entities.
 * It provides an abstraction layer over the standard database interactions, offering custom queries for User entities.
 * Utilizing Spring Data JPA, it simplifies the data access logic and provides standard CRUD operations.
 *
 * @see JpaRepository
 */
public interface UserPermissionRepository extends JpaRepository<UserPermission, String> {

    /**
     * Retrieves a list of {@link UserPermission} entities for a specific user
     * where the entity type does not match the specified filter.
     *
     * @param userId The ID of the user whose permissions are being queried.
     * @param entityFilter The entity type to be excluded from the results.
     * @return A list of {@link UserPermission} entities matching the criteria.
     */
    List<UserPermission> findAllByUserIdAndEntityIsNot(UUID userId, String entityFilter);

    /**
     * Checks whether any {@link UserPermission} entities match the given JPA specification.
     *
     * @param specification A {@link Specification<UserPermission>} object defining the criteria
     *                      for the query.
     * @return true if at least one {@link UserPermission} entity matches the specification; false otherwise.
     */
    boolean exists(Specification<UserPermission> specification);
}
