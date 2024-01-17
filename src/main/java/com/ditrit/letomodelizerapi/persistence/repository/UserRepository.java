package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository interface extends JpaRepository for handling data persistence operations for User entities.
 * It provides an abstraction layer over the standard database interactions, offering custom queries for User entities.
 * Utilizing Spring Data JPA, it simplifies the data access logic and provides standard CRUD operations.
 *
 * @see JpaRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their login.
     * This method returns an {@code Optional<User>} to handle cases where a user may not exist.
     *
     * @param login the login of the user to be retrieved.
     * @return an Optional containing the User if found, or an empty Optional otherwise.
     */
    Optional<User> findByLogin(String login);
}
