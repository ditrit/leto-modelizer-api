package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for {@link AccessControlPermission} entities. This interface extends
 * {@link JpaRepository}, providing standardized CRUD operations and additional methods for interacting with the
 * persistence layer. It specifically handles operations related to {@code AccessControlPermission} entities,
 * facilitating access control and permission management within the application.
 */
public interface AccessControlPermissionRepository extends JpaRepository<AccessControlPermission, Long> {
    /**
     * Finds an {@link AccessControlPermission} entity based on a combination of access control ID and permission ID.
     * This method is useful for determining if a specific permission has already been assigned to an access control
     * entry, enabling precise control over access permissions.
     *
     * @param accessControlId The ID of the access control for which the permission is being queried.
     * @param permissionId The ID of the permission being queried.
     * @return An {@code Optional<AccessControlPermission>} containing the matching entity if found; otherwise, an
     *         empty {@code Optional} is returned. This allows for easy handling of cases where no matching
     *         permission assignment exists.
     */
    Optional<AccessControlPermission> findByAccessControlIdAndPermissionId(Long accessControlId, Long permissionId);
}
