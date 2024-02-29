package com.ditrit.letomodelizerapi.model.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object of the user permission model, including only the essential fields.
 */
@Data
public class PermissionDTO {
    /**
     * The unique identifier of the Permission entity.
     * This field represents the primary key in the database.
     */
    private UUID id;

    /**
     * The entity to which the permission is related.
     */
    private String entity;

    /**
     * The action for which the permission is granted.
     */
    private String action;

    /**
     * The id of the library to which this permission is related.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID libraryId;
}
