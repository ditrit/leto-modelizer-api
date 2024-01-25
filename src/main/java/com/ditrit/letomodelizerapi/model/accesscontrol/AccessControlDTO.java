package com.ditrit.letomodelizerapi.model.accesscontrol;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for AccessControl.
 * This class is used for transferring AccessControl data between different layers of the application,
 * typically between services and controllers. It is designed to encapsulate the data attributes of an AccessControl
 * entity in a form that is easy to serialize and deserialize when sending responses or requests.
 */
@Data
public class AccessControlDTO {
    /**
     * The unique identifier of the AccessControl entity.
     * This field represents the primary key in the database.
     */
    private Long id;
    /**
     * The name of the AccessControl entity.
     * This field can be used to display or refer to the AccessControl entity in the user interface.
     */
    private String name;
}
