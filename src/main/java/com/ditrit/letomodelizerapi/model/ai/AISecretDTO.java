package com.ditrit.letomodelizerapi.model.ai;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for AISecret.
 * This class is used for transferring AISecret data between different layers of the application,
 * typically between services and controllers. It is designed to encapsulate the data attributes of a
 * AISecret entity in a form that is easy to serialize and deserialize when sending responses or requests.
 */
@Data
public class AISecretDTO {
    /**
     * The unique identifier of the AISecret entity.
     * This field represents the primary key in the database.
     */
    private UUID id;
    /**
     * The key of the AISecret entity.
     * This field can be used to display or refer to the AISecret entity in the user interface.
     */
    private String key;

    /**
     * The last update date of the AISecret.
     */
    private Timestamp updateDate;
}
