package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermissionView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for AccessControlPermissionView entity.
 * This interface extends JpaRepository, inheriting standard CRUD operations for AccessControlPermissionView entities.
 * It also provides additional methods to find AccessControl entities based on specifications.
 */
public interface AccessControlPermissionViewRepository extends JpaRepository<AccessControlPermissionView, Long> {

    /**
     * Retrieves a page of AccessControlPermissionView entities that match the given specification.
     * This method allows for complex queries and filtering of AccessControlPermissionView records using the provided
     * specification.
     *
     * @param specification a Specification object that defines the conditions for filtering AccessControlPermissionView
     *                      records
     * @param pageable a Pageable object that defines the pagination parameters
     * @return a Page containing AccessControlPermissionView entities that match the given specification
     */
    Page<AccessControlPermissionView> findAll(Specification<AccessControlPermissionView> specification,
                                              Pageable pageable);
}
