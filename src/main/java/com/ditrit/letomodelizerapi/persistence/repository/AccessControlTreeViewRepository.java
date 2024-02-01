package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AccessControlTreeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository interface for {@link AccessControlTreeView} entities.
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * and allows for dynamic query construction using {@link Specification} for {@link AccessControlTreeView} entities.
 * It's designed to handle data access and manipulation for views representing hierarchical structures
 * of access control entities.
 */
public interface AccessControlTreeViewRepository extends JpaRepository<AccessControlTreeView, String> {

    /**
     * Retrieves a paginated list of {@link AccessControlTreeView} entities based on the provided {@link Specification}.
     * This method supports complex queries and filtering through specifications, allowing for flexible retrieval
     * of access control tree views based on dynamic search criteria.
     *
     * @param specification a {@link Specification} object that defines the criteria for filtering the entities.
     * @param pageable a {@link Pageable} object that contains pagination information.
     * @return a {@link Page} of {@link AccessControlTreeView} entities that match the given specification.
     */
    Page<AccessControlTreeView> findAll(Specification<AccessControlTreeView> specification, Pageable pageable);

    /**
     * Finds an {@link AccessControlTreeView} entity based on a combination of access control ID and parent access
     * control ID.
     * This method is useful for identifying specific hierarchical relationships within the access control tree
     * structure.
     *
     * @param id the access control ID of the current entity.
     * @param parent the parent access control ID in the hierarchy.
     * @return an {@link Optional} containing the found {@link AccessControlTreeView} entity, or an empty Optional
     *         if no entity matches the provided IDs.
     */
    Optional<AccessControlTreeView> findByAccessControlIdAndParentAccessControlId(Long id, Long parent);
}
