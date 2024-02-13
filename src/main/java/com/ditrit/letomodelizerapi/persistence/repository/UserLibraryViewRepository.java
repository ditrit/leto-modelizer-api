package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserLibraryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository interface for {@link UserLibraryView} entities. This interface extends
 * {@link JpaRepository}, providing standardized CRUD operations and additional methods for interacting with the
 * persistence layer. It specifically handles operations related to {@code UserLibraryView} entities,
 * facilitating access control and permission management within the application.
 */
public interface UserLibraryViewRepository extends JpaRepository<UserLibraryView, Long> {

    /**
     * Finds all {@code UserLibraryView} entities associated with a given user ID, applying a specification for
     * complex queries and filtering, and a pageable object for pagination.
     *
     * @param userId the ID of the user to whom the libraries are assigned
     * @param specification a {@code Specification<UserLibraryView>} object that defines the conditions for
     * filtering user library view records
     * @param pageable a {@code Pageable} object that defines the pagination parameters
     * @return a {@code Page<UserLibraryView>} containing {@code UserLibraryView} entities that match
     * the given user ID and specification
     */
    Page<UserLibraryView> findAllByUserId(Long userId,
                                          Specification<UserLibraryView> specification,
                                          Pageable pageable);
}
