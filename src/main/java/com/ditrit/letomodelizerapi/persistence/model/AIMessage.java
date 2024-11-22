package com.ditrit.letomodelizerapi.persistence.model;

import io.github.zorin95670.predicate.FilterType;
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
 * Entity representing an AI message associated with an AI conversation.
 * This class maps to the 'ai_messages' table in the database, storing message between AI and user.
 */
@Entity
@Table(name = "ai_messages")
@EqualsAndHashCode(callSuper = true)
@Data
public class AIMessage extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aim_id")
    @FilterType(type = UUID.class)
    private UUID id;

    /**
     * The identifier of the conversation.
     */
    @Column(name = "aic_id")
    @FilterType(type = UUID.class)
    private UUID aiConversation;

    /**
     * Indicate the author of the message: if true, the message is from the user; otherwise, it is from the AI.
     */
    @Column(name = "is_user")
    private Boolean isUser;

    /**
     * Base64 of compressed message.
     */
    @Column(name = "message")
    private byte[] message;

    /**
     * Set insertDate before persisting in repository.
     */
   @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
