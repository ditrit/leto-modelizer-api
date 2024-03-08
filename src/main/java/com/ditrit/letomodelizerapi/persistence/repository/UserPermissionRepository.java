package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Retrieves a page of UserPermission entities that match the given specification.
     * This method allows for complex queries and filtering of UserPermission records using the provided Specification.
     * It is designed to support advanced search operations, enabling the dynamic composition of query conditions.
     * Through the use of the Pageable interface, it also supports pagination and sorting, allowing clients to specify
     * how the results should be returned (e.g., ordering, page size, current page).
     *
     * @param specification a Specification<UserPermission> object that defines the conditions for filtering
     *                      UserPermission records.
     * @param pageable a Pageable object that defines the pagination and sorting parameters.
     * @return a Page<UserPermission> containing the UserPermission entities that match the given specification and
     * pagination settings.
     */
    Page<UserPermission> findAll(Specification<UserPermission> specification, Pageable pageable);

    /**
     * Checks whether any {@link UserPermission} entities match the given JPA specification.
     *
     * @param specification A {@link Specification<UserPermission>} object defining the criteria
     *                      for the query.
     * @return true if at least one {@link UserPermission} entity matches the specification; false otherwise.
     */
    boolean exists(Specification<UserPermission> specification);
}
