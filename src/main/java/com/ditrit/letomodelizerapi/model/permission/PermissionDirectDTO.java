package com.ditrit.letomodelizerapi.model.permission;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object (DTO) for Permission entities with a focus on direct relationship status.
 * Extends {@link PermissionDTO} to include additional information about the directness of the
 * relationship within the access control hierarchy.
 * @see PermissionDTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionDirectDTO extends PermissionDTO {

    /**
     * Indicates whether the permission relationship is direct.
     * This field distinguishes between direct and indirect relationships in the access control tree.
     * A direct relationship implies that the current access control is immediately linked to the permission,
     * without any intermediate access controls in between. This is useful for understanding the immediate
     * hierarchical structure and for operations that depend on direct lineage.
     */
    private Boolean isDirect;
}
