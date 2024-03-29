package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTreeView;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.function.Function;

/**
 * Implements a {@link Function} that converts an {@link AccessControlTreeView} entity into an
 * {@link AccessControlDirectDTO} object.
 * This function is designed to map data from a hierarchical view of access controls into a data transfer object that
 * includes information about the directness of the access control relationship.
 */
@AllArgsConstructor
@NoArgsConstructor
public class AccessControlTreeViewToAccessControlDirectDTOFunction implements Function<AccessControlTreeView,
        AccessControlDirectDTO> {

    /**
     * Indicates whether the access control information should be derived from the parent access control.
     * When set to {@code true}, the {@link AccessControlDirectDTO} produced by the {@code apply} method will contain
     * the ID and name from the parent access control of the provided {@link AccessControlTreeView}.
     * When {@code false}, the DTO will instead contain the ID and name from the access control itself.
     * <p>
     * This field is used to dynamically determine the source of access control information based on the
     * hierarchical relationship between access controls. It defaults to {@code true}, favoring parent access control
     * information to populate the DTO.
     */
    private boolean fromParent = true;

    @Override
    public AccessControlDirectDTO apply(final AccessControlTreeView accessControlTreeView) {
        AccessControlDirectDTO dto = new AccessControlDirectDTO();

        if (this.fromParent) {
            dto.setId(accessControlTreeView.getParentId());
            dto.setName(accessControlTreeView.getParentName());
        } else {
            dto.setId(accessControlTreeView.getId());
            dto.setName(accessControlTreeView.getName());
        }

        dto.setIsDirect(accessControlTreeView.getIsDirect());

        return dto;
    }
}
