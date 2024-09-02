package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.persistence.specification.filter.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an AI conversation associated with a user.
 * This class maps to the 'ai_conversations' table in the database, storing AI conversations that are used
 * to register all conversation with AI for a user.
 */
@Entity
@Table(name = "ai_conversations")
@EqualsAndHashCode(callSuper = true)
@Data
public class AIConversation extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aic_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID id;

    /**
     * The login identifier of the user to whom the conversation is associated.
     */
    @Column(name = "usr_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID userId;

    /**
     * The conversation key.
     */
    @Column(name = "key")
    @FilterType(type = FilterType.Type.TEXT)
    private String key;

    /**
     * Checksum of last diagram files sent.
     */
    @Column(name = "checksum")
    private String checksum;

    /**
     * The conversation context for AI.
     */
    @Column(name = "context")
    private String context;

    /**
     * Size of all conversation messages.
     */
    @Column(name = "size")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long size;

    /**
     * Set insertDate before persisting in repository.
     */
   @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
