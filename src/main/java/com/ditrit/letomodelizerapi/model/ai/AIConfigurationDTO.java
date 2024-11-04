package com.ditrit.letomodelizerapi.model.ai;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for AIConfiguration.
 * This class is used for transferring AIConfiguration data between different layers of the application,
 * typically between services and controllers. It is designed to encapsulate the data attributes of a
 * AIConfiguration entity in a form that is easy to serialize and deserialize when sending responses or requests.
 */
@Data
public class AIConfigurationDTO {
    /**
     * The unique identifier of the AIConfiguration entity.
     * This field represents the primary key in the database.
     */
    private UUID id;
    /**
     * The handler of the AIConfiguration entity.
     * This field can be used to display or refer to the AIConfiguration entity in the user interface.
     */
    private String handler;
    /**
     * The key of the AIConfiguration entity.
     * This field can be used to display or refer to the AIConfiguration entity in the user interface.
     */
    private String key;
    /**
     * The value of the AIConfiguration entity.
     * This field can be used to display or refer to the AIConfiguration entity in the user interface.
     */
    private String value;
    /**
     * The last update date of the AIConfiguration.
     */
    private Timestamp updateDate;
}
