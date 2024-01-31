package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserAccessControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserAccessControlRepository interface extends JpaRepository for handling data persistence operations for
 * UserAccessControl entities.
 * It provides an abstraction layer over the standard database interactions, offering custom queries for
 * UserAccessControl entities.
 * Utilizing Spring Data JPA, it simplifies the data access logic and provides standard CRUD operations.
 *
 * @see JpaRepository
 */
public interface UserAccessControlRepository extends JpaRepository<UserAccessControl, Long> {
    /**
     * Finds and returns an Optional of UserAccessControl entity based on the provided accessControlId and userId.
     * This method allows for retrieving a UserAccessControl entity when both the ID of the access control and the ID
     * of the user are known.
     * If no entity matches the provided IDs, an empty Optional is returned.
     *
     * @param accessControlId the ID of the AccessControl entity
     * @param userId the ID of the User entity
     * @return an Optional containing the found UserAccessControl entity or an empty Optional if no entity is found
     */
    Optional<UserAccessControl> findByAccessControlIdAndUserId(Long accessControlId, Long userId);
}
