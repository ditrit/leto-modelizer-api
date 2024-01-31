package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserAccessControlViewRepository interface extends JpaRepository for handling data persistence operations for
 * UserAccessControlView entities.
 * It provides an abstraction layer over the standard database interactions, offering custom queries for
 * UserAccessControlView entities.
 * Utilizing Spring Data JPA, it simplifies the data access logic and provides standard CRUD operations.
 *
 * @see JpaRepository
 */
public interface UserAccessControlViewRepository extends JpaRepository<UserAccessControlView, String> {
    /**
     * Retrieves a paginated list of UserAccessControlView entities that match the given specification.
     * This method allows for complex queries to be constructed and passed in as a Specification,
     * and the results are returned in a paginated format based on the Pageable argument.
     *
     * @param specification a Specification object that defines the criteria for filtering the UserAccessControlView
     *                      entities
     * @param pageable a Pageable object that contains the pagination information
     * @return a Page of UserAccessControlView entities that match the given specification
     */
    Page<UserAccessControlView> findAll(Specification<UserAccessControlView> specification, Pageable pageable);
}
