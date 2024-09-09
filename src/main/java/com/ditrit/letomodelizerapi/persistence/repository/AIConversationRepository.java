package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.AIConversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository interface for AIConversation entities.
 * This interface extends JpaRepository, providing standard CRUD operations for AIConversation entities.
 */
public interface AIConversationRepository extends JpaRepository<AIConversation, UUID> {
    /**
     * Checks if a AIConversation with the specified user Id and key already exists in the database.
     *
     * @param userId the userId to check for existence
     * @param key the key to check for existence
     * @return true if a AIConversation with the user Id and key exists, false otherwise
     */
    boolean existsByUserIdAndKey(UUID userId, String key);

    /**
     * Finds an AIConversation by its id and userId.
     *
     * @param id the id of the AIConversation.
     * @param userId the userId associated with the AIConversation.
     * @return an Optional containing the AIConversation if found, or an empty Optional if not found.
     */
    Optional<AIConversation> findByIdAndUserId(UUID id, UUID userId);

    /**
     * Finds all AIConversations that match the provided specification, with pagination support.
     *
     * @param specification the specification to filter AIConversations.
     * @param pageable the pagination information.
     * @return a Page containing the AIConversations that match the specification.
     */
    Page<AIConversation> findAll(Specification<AIConversation> specification, Pageable pageable);
}
