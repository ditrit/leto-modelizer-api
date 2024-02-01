package com.ditrit.letomodelizerapi.model.accesscontrol;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object (DTO) for AccessControl entities with a focus on direct relationship status.
 * Extends {@link AccessControlDTO} to include additional information about the directness of the
 * relationship within the access control hierarchy.
 * @see AccessControlDTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessControlDirectDTO extends AccessControlDTO {

    /**
     * Indicates whether the access control relationship is direct.
     * This field distinguishes between direct and indirect relationships in the access control tree.
     * A direct relationship implies that the current access control is immediately subordinate to the parent,
     * without any intermediate access controls in between. This is useful for understanding the immediate
     * hierarchical structure and for operations that depend on direct lineage.
     */
    private Boolean isDirect;
}
