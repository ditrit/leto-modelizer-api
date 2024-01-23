package com.ditrit.letomodelizerapi.model.user.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Data Transfer Object of the user permission model, including only the essential fields.
 */
@Data
public class UserPermissionDTO {

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
    private Long libraryId;
}
