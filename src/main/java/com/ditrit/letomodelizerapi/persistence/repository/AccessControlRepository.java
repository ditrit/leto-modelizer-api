package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for AccessControl entity.
 * This interface extends JpaRepository, inheriting standard CRUD operations for AccessControl entities.
 * It also provides additional methods to find AccessControl entities based on specifications.
 */
public interface AccessControlRepository extends JpaRepository<AccessControl, UUID> {
    /**
     * Retrieves a page of AccessControl entities that match the given specification.
     * This method allows for complex queries and filtering of AccessControl records using the provided specification.
     *
     * @param specification a Specification object that defines the conditions for filtering AccessControl records
     * @param pageable a Pageable object that defines the pagination parameters
     * @return a Page containing AccessControl entities that match the given specification
     */
    Page<AccessControl> findAll(Specification<AccessControl> specification, Pageable pageable);

    /**
     * Retrieves a single AccessControl entity that matches the given specification.
     * If multiple records match the specification, the method returns the first found.
     * If no records match, it returns an empty Optional.
     *
     * @param specification a Specification object that defines the conditions for filtering AccessControl records
     * @return an Optional containing the found AccessControl entity, or an empty Optional if no match is found
     */
    Optional<AccessControl> findOne(Specification<AccessControl> specification);

    /**
     * Finds an AccessControl entity based on the provided name.
     * This method searches for an AccessControl entity that matches the given name. It is typically used to retrieve
     * access control records from the database, such as roles or permissions, by their unique names.
     *
     * @param name the name of the AccessControl entity to search for.
     * @return the found AccessControl entity matching the given name, or null if no such entity exists.
     */
    AccessControl findByName(String name);
}
