package com.ditrit.letomodelizerapi.model.ai;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for AIMessage.
 * This class is used for transferring AIMessage data between different layers of the application,
 * typically between services and controllers. It is designed to encapsulate the data attributes of an AIMessage
 * entity in a form that is easy to serialize and deserialize when sending responses or requests.
 */
@Data
public class AIMessageDTO {
    /**
     * The unique identifier of the AIConversation entity.
     * This field represents the primary key in the database.
     */
    private UUID id;
    /**
     * The conversation uuid of the AIConversation entity.
     */
    private UUID aiConversation;

    /**
     * Indicate the author of the message: if true, the message is from the user; otherwise, it is from the AI.
     */
    private Boolean isUser;

    /**
     * The message.
     */
    private String message;

    /**
     * The last update date of the message.
     */
    private Timestamp updateDate;
}
