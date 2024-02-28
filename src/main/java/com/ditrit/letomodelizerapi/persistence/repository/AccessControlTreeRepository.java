package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AccessControlTree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link AccessControlTree} entity.
 * This interface provides the mechanism for CRUD operations and custom queries on the
 * access control tree entities within the database. It extends {@link JpaRepository},
 * leveraging Spring Data's repository abstraction to simplify data access layers.
 *
 * @see JpaRepository
 */
public interface AccessControlTreeRepository extends JpaRepository<AccessControlTree, UUID> {

    /**
     * Finds an {@link AccessControlTree} entity based on parent and current access control identifiers.
     * This method allows for the retrieval of a specific access control tree entity that matches
     * the given parent and current access control IDs. It's useful for operations that require
     * understanding or modifying the hierarchical relationship between access controls.
     *
     * @param parent the parent access control identifier.
     * @param current the current access control identifier.
     * @return an {@link Optional} containing the found {@link AccessControlTree} entity, or an empty Optional
     *         if no entity matches the provided identifiers.
     */
    Optional<AccessControlTree> findByParentAndCurrent(UUID parent, UUID current);
}
