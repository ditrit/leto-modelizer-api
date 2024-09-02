package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AIMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository interface for AIMessage entities.
 * This interface extends JpaRepository, providing standard CRUD operations for AIMessage entities.
 */
public interface AIMessageRepository extends JpaRepository<AIMessage, UUID> {
    /**
     * Finds all AIMessages that match the provided specification, with pagination support.
     *
     * @param specification the specification to filter AIMessages.
     * @param pageable the pagination information.
     * @return a Page containing the AIMessages that match the specification.
     */
    Page<AIMessage> findAll(Specification<AIMessage> specification, Pageable pageable);
}
