package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository interface extends JpaRepository for handling data persistence operations for User entities.
 * It provides an abstraction layer over the standard database interactions, offering custom queries for User entities.
 * Utilizing Spring Data JPA, it simplifies the data access logic and provides standard CRUD operations.
 *
 * @see JpaRepository
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Retrieves a user by their login.
     * This method returns an {@code Optional<User>} to handle cases where a user may not exist.
     *
     * @param login the login of the user to be retrieved.
     * @return an Optional containing the User if found, or an empty Optional otherwise.
     */
    Optional<User> findByLogin(String login);

    /**
     * Finds all users matching the given specification with pagination support.
     * This method allows for complex queries and dynamic filtering of User entities,
     * utilizing the JPA criteria API encapsulated by the {@link Specification} interface.
     *
     * @param specification The criteria for filtering users, encapsulated in a {@link Specification<User>}.
     * @param pageable The pagination information and sorting criteria.
     * @return A {@link Page<User>} containing users that match the given specification.
     */
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
