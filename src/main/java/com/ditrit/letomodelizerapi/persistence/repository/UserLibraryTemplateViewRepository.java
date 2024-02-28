package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserLibraryTemplateView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository interface for {@link UserLibraryTemplateView} entities. This interface extends
 * {@link JpaRepository}, providing standardized CRUD operations and additional methods for interacting with the
 * persistence layer. It specifically handles operations related to {@code UserLibraryTemplateView} entities,
 * facilitating access control and permission management within the application.
 */
public interface UserLibraryTemplateViewRepository extends JpaRepository<UserLibraryTemplateView, String> {

    /**
     * Finds all {@code UserLibraryTemplateView} entities associated with a given user ID, applying a specification for
     * complex queries and filtering, and a pageable object for pagination.
     *
     * @param userId the ID of the user to whom the library templates are assigned
     * @param specification a {@code Specification<UserLibraryTemplateView>} object that defines the conditions for
     * filtering user library template view records
     * @param pageable a {@code Pageable} object that defines the pagination parameters
     * @return a {@code Page<UserLibraryTemplateView>} containing {@code UserLibraryTemplateView} entities that match
     * the given user ID and specification
     */
    Page<UserLibraryTemplateView> findAllByUserId(UUID userId,
                                                  Specification<UserLibraryTemplateView> specification,
                                                  Pageable pageable);
}
