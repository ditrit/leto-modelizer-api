package com.ditrit.letomodelizerapi.model.ai;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for AIConversation.
 * This class is used for transferring AIConversation data between different layers of the application,
 * typically between services and controllers. It is designed to encapsulate the data attributes of an AIConversation
 * entity in a form that is easy to serialize and deserialize when sending responses or requests.
 */
@Data
public class AIConversationDTO {
    /**
     * The unique identifier of the AIConversation entity.
     * This field represents the primary key in the database.
     */
    private UUID id;
    /**
     * The conversation key of the AIConversation entity.
     */
    private String key;

    /**
     * Checksum of last diagram files sent.
     */
    private String checksum;

    /**
     * Checksum of last diagram files sent.
     */
    private Long size;

    /**
     * The last update date of the conversation.
     */
    private Timestamp updateDate;
}
